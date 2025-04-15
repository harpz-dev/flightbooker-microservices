import React, { useState, useEffect, useRef } from 'react';
import { SEARCH_FLIGHT_URL } from '../config/apiUrls';
import { bookingServiceApi, flightServiceApi } from '../config/axiosInstances';
import { paymentServiceApi} from '../config/axiosInstances';
import { useGlobalState } from '../context/GlobalStateContext';
import SignOutButton from '../components/SignoutButton';
import DarkModeButton from '../components/DarkModeButton';
import './Dashboard.css';  // Make sure to create this CSS file for styling

const Dashboard = () => {
  const [source, setSource] = useState('');
  const [destination, setDestination] = useState('');
  const [departureDate, setDepartureDate] = useState('');
  const [flights, setFlights] = useState([]);
  const [error, setError] = useState('');
  const [sourceCities, setSourceCities] = useState([]);  // State for source cities
  const [destinationCities, setDestinationCities] = useState([]);  // State for destination cities

  const [activeTab, setActiveTab] = useState('bookFlights');  // Tracks which tab is active
  
  const bookingIDRef = useRef(null);
  const { setIsLoggedIn } = useGlobalState();
  
  const [showBookingModal, setShowBookingModal] = useState(false);
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [seatsToBook, setSeatsToBook] = useState(1);

  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [paymentOption, setPaymentOption] = useState(null);  // Track selected payment option
  const [paymentStatusMessage, setPaymentStatusMessage] = useState(null); // New state to track payment status message
  const [isPaymentSuccessful, setIsPaymentSuccessful] = useState(false);
  const [paymentAmount, setPaymentAmount]= useState('');


  const [isProcessing, setIsProcessing] = useState(false);

  const [bookingHistory, setBookingHistory] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(false);

  const sleep = (ms) => {
    return new Promise(resolve => setTimeout(resolve, ms));
  };


   // Fetch source cities when the component mounts
   useEffect(() => {
    const fetchSourceCities = async () => {
      try {
        const response = await flightServiceApi('/sources')
        const data = await response.data;
        setSourceCities(data); // Assuming the response is an array of source cities
      } catch (err) {
        console.error('Error fetching source cities:', err);
      }
    };

    fetchSourceCities();
  }, []);

  // Fetch destination cities when the component mounts
  useEffect(() => {
    const fetchDestinationCities = async () => {
      try {
        const response = await flightServiceApi('/destinations'); // Endpoint for destination cities
        const data = await response.data;
        setDestinationCities(data); // Assuming the response is an array of destination cities
      } catch (err) {
        console.error('Error fetching destination cities:', err);
      }
    };

    fetchDestinationCities();
  }, []);
  
  const handleSearch = async () => {
    try {
      const response = await fetch(`${SEARCH_FLIGHT_URL}?source=${source}&destination=${destination}&departureDate=${departureDate}`);

      if (!response.ok) {
        throw new Error('Error fetching flights.');
      }

      const data = await response.json();
      setFlights(data);

      if(data.length==0){
        setError('No matching flights found');
      }
      else{
      setError('');
      }
    } catch (err) {
      setError('Error fetching flights.');
    }
  };

  const handleBookFlight = (flightId) => {
    const flight = flights.find(f => f.id === flightId);
    setSelectedFlight(flight);
    setSeatsToBook(1);  // reset to 1 seat by default
    setShowBookingModal(true);
    
    // Reset payment states when booking a new flight
    setShowPaymentModal(false);  
    setPaymentOption(null);  
    setPaymentStatusMessage(null);  
    setIsPaymentSuccessful(false); 
  };

  const handleConfirmBooking = async () => {
    if(isProcessing)
      return
    setIsProcessing(true);

    try {
      const response = await bookingServiceApi.post('', {
        requestType: 'FLIGHT_BOOKING',
        details: {
          flightId: Number(selectedFlight.id),
          numSeats: Number(seatsToBook)
        },
      });

      if (response.status === 202) {
        bookingIDRef.current= response.data.bookingId;

        console.log('Handle booking id: '+ bookingIDRef.current)
        await sleep(2000);  // Simulate delay for booking status retrieval
        const statusResponse = await bookingServiceApi.get(`/status/${bookingIDRef.current}`);

        const statusResponseString = statusResponse.data;

        // Check if the booking status is "RESERVED", and show payment modal if true
        if (statusResponseString === "RESERVED") {
          setShowPaymentModal(true);
          handleSearch(); //update the table based on booked flights
          const response= await bookingServiceApi.get(`/${bookingIDRef.current}`);
          setPaymentAmount(response.data.amount);
        }
      }
    } catch (error) {
      alert(`Unexpected error occured, please try to refresh the page and try again`);
      // handle error
      console.error(error);
    }
   
    setIsProcessing(false);
    setShowBookingModal(false);
  };

  const handleCloseBookingModal= ()=>{
    setIsProcessing(false);
     setShowBookingModal(false);
  }

  const handlePayment = async () => {
    
    if(paymentOption==null){
      setPaymentStatusMessage('Please select a payment option');
      return;
    }

    if(isProcessing)
      return;
    
    setIsProcessing(true);

    try {

      console.log('Handle payment id: '+ bookingIDRef.current)
      
        const response= await paymentServiceApi.post(`/process`, {
          
          method: paymentOption,
          bookingId: bookingIDRef.current
          
        });

        const paymentId = response.data;
        await sleep(2000); 
        const paymentStatusResponse= await paymentServiceApi.get(`/${paymentId}`, {
      
        });
        const paymentStatus= paymentStatusResponse.data.paymentStatus;

        if (paymentStatus === 'SUCCESSFUL') {
          setIsPaymentSuccessful(true); // Set payment as successful
          setPaymentStatusMessage('Payment successful! Your boarding pass will be emailed to your provided email ID!');
        } else {
          setPaymentStatusMessage('There was an error with your payment. Please try again.');
          setIsProcessing(false);
        }
    
      } catch (error) {
        console.error('Payment error:', error);
        setPaymentStatusMessage('There was an error with the payment process. Please try again.');
        setIsProcessing(false);
      }
  };

  const handleClosePaymentModal = () => {
    setIsProcessing(false);
    setShowPaymentModal(false);
    setPaymentOption(null); 
    setPaymentStatusMessage(null);  
    setIsPaymentSuccessful(false);  
    setPaymentAmount('');//
  };

  const filterCities = (input, cities) => {
    return cities.filter(city => city.toLowerCase().includes(input.toLowerCase()));
  };

  const fetchBookingHistory = async () => {
    setLoadingHistory(true);
    try {
      const response = await bookingServiceApi.get('/history');
      setBookingHistory(response.data);
    } catch (err) {
      console.error('Error fetching booking history:', err);
    }
    setLoadingHistory(false);
  };
  
  useEffect(() => {
    if (activeTab === 'bookingHistory') {
      fetchBookingHistory();
    }
  }, [activeTab]);



  return (
    <div className="dashboard">
  <div className="header">
    <h2>Dashboard</h2>
    <div className="profile-dropdown">
      <button className="profile-btn">Profile</button>
      <div className="dropdown-content">
        <SignOutButton />
        <DarkModeButton />
      </div>
    </div>
  </div>

  <div className="tabs">
    <button
      className={activeTab === 'bookFlights' ? 'tab active' : 'tab'}
      onClick={() => setActiveTab('bookFlights')}
    >
      Book Flights
    </button>
    <button
      className={activeTab === 'bookingHistory' ? 'tab active' : 'tab'}
      onClick={() => setActiveTab('bookingHistory')}
    >
      Booking History
    </button>
  </div>

  {activeTab === 'bookFlights' && (
    <div className="book-flights-tab">
      <h3>Search for Flights</h3>
      <div className="search-form">
        <input
          type="text"
          placeholder="Source"
          value={source}
          onChange={(e) => setSource(e.target.value)}
          list="sourceCities"
        />
        <datalist id="sourceCities">
              {filterCities(source, sourceCities).map((city, index) => (
                <option key={index} value={city} />
              ))}
            </datalist>
        <input
          type="text"
          placeholder="Destination"
          value={destination}
          onChange={(e) => setDestination(e.target.value)}
           list="destinationCities"
        />
         <datalist id="destinationCities">
              {filterCities(destination, destinationCities).map((city, index) => (
                <option key={index} value={city} />
              ))}
            </datalist>

        <input
          type="date"
          value={departureDate}
          onChange={(e) => setDepartureDate(e.target.value)}
        />
        <button onClick={handleSearch}>Search</button>
      </div>

      {error && <p>{error}</p>}

      {flights.length > 0 && (
        <div className="flight-list">
          <h3>Available Flights</h3>
          <table>
            <thead>
              <tr>
                <th>Flight Number</th>
                <th>Airline</th>
                <th>Source</th>
                <th>Destination</th>
                <th>Departure Time</th>
                <th>Seats Available</th>
                <th>Price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {flights.map((flight) => (
                <tr key={flight.flightNumber}>
                  <td>{flight.flightNumber}</td>
                  <td>{flight.airline}</td>
                  <td>{flight.source}</td>
                  <td>{flight.destination}</td>
                  <td>{new Date(flight.departureTime).toLocaleString()}</td>
                  <td>{flight.availableSeats}</td>
                  <td>{flight.price}</td>
                  <td>
                    <button onClick={() => handleBookFlight(flight.id)}>Book</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )}

  {activeTab === 'bookingHistory' && (
    <div className="booking-history-tab">
      <h3>Your Booking History</h3>
     
  {loadingHistory ? (
    <p>Loading booking history...</p>
  ) : bookingHistory.length > 0 ? (
    <table className="booking-history">
    <thead>
      <tr>
        <th>Booking ID</th>
        <th>Seats Booked</th>
        <th>Status</th>
        <th>Amount Paid</th>
      </tr>
    </thead>
    <tbody>
      {bookingHistory.map((booking) => (
        <tr key={booking.id}>
          <td>{booking.bookingId}</td>
          <td>{booking.details.numSeats}</td>
          <td>{booking.status}</td>
          <td>${booking.amount}</td>
        </tr>
      ))}
    </tbody>
  </table>
  ) : (
    <p>No booking history available yet.</p>
  )}
</div>
    
  )}

  {/* Booking Modal */}
  {showBookingModal && selectedFlight && (
    <div className="booking-modal">
      <div className="modal-content">
        <h3>Booking Flight {selectedFlight.flightNumber}</h3>
        <p>Airline: {selectedFlight.airline}</p>
        <p>Available Seats: {selectedFlight.availableSeats}</p>

        <div className="seat-selector">
          <button onClick={() => setSeatsToBook(Math.max(1, seatsToBook - 1))}>-</button>
          <span>{seatsToBook}</span>
          <button onClick={() => setSeatsToBook(Math.min(selectedFlight.availableSeats, seatsToBook + 1))}>+</button>
        </div>

        <div className="modal-actions">
          <button className='confirm' disabled={isProcessing}  onClick={handleConfirmBooking}>Confirm Booking</button>
          <button className= 'cancel' disabled={isProcessing}  onClick={handleCloseBookingModal}>Cancel</button>
        </div>
      </div>
    </div>
  )}

{/* Payment Modal */}
{showPaymentModal && (
  <div className="payment-modal">
    <div className="modal-content">
      <h3>Complete Your Payment</h3>
      {!isPaymentSuccessful && (<div>
        <h4>Amount Due: ${paymentAmount}</h4>
        <hr/>
        <p>Please choose your payment method:</p>
      </div>)}

      {!isPaymentSuccessful && (
        <div className="payment-options">
          <button 
            onClick={() => setPaymentOption('PAYPAL')}
            className={paymentOption === 'PAYPAL' ? 'selected' : ''}
          >
            Pay with PayPal
          </button>
          <button 
            onClick={() => setPaymentOption('CREDIT_CARD')}
            className={paymentOption === 'CREDIT_CARD' ? 'selected' : ''}
          >
            Pay with Credit Card
          </button>
        </div>
      )}

      {!isPaymentSuccessful && (
        <div className="modal-actions">
          <button
            disabled={isProcessing} 
            onClick={handlePayment}
            className="confirm"
          >
            Proceed with Payment
          </button>
          <button 
           disabled={isProcessing} 
            onClick={() => setShowPaymentModal(false)}
            className="cancel"
          >
            Cancel
          </button>
        </div>
      )}

      {paymentStatusMessage && (
        <div className="payment-status-message">
          <p>{paymentStatusMessage}</p>
          {isPaymentSuccessful && <button className= "closePaymentSuccess" onClick={handleClosePaymentModal}>Close</button>}
        </div>
      )}
    </div>
  </div>
)}

</div>

  );
};

export default Dashboard;
