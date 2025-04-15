import axios from 'axios';
import * as API_URLS from './apiUrls'; // Import your API URLs from config

// Function to create a reusable Axios instance for any service
const createAxiosInstance = (serviceUrl) => {
  const apiInstance = axios.create({
    baseURL: serviceUrl,  // Base URL passed dynamically
    withCredentials: true,  // Include cookies (for HttpOnly cookie handling)
    headers: {
      'Content-Type': 'application/json',  // Default content type for JSON
    },
  });

  // Add request interceptor to include access token from localStorage
  apiInstance.interceptors.request.use(
    (config) => {
      const accessToken = localStorage.getItem('accessToken');
      if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;  // Add token to the header if it exists
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Add response interceptor to handle token refresh when receiving a 401 error
  apiInstance.interceptors.response.use(
    (response) => {
      return response; // If the request is successful, return the response
    },
    async (error) => {
      const originalRequest = error.config;

      // If the error is a 401 Unauthorized and we haven't tried refreshing yet
      if (error.response && error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true; // Mark this request as having been retried
        try {
          // Request to refresh the access token (endpoint may vary)
          const refreshResponse = await axios.post(
            `${API_URLS.REFRESH_URL}`,  // Adjust the URL based on your Auth API
            {},
            { withCredentials: true }  // Ensure cookies are included with the request
          );

          // If the refresh is successful, update the access token in localStorage
          const newAccessToken = refreshResponse.data.accessToken;
          localStorage.setItem('accessToken', newAccessToken);

          // Set the new access token in the Authorization header
          originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

          // Retry the original request with the new token
          return axios(originalRequest);
        } catch (refreshError) {
          // Handle errors with refreshing the token (e.g., log out the user)
          
          localStorage.removeItem('accessToken');
          localStorage.setItem('isLoggedIn', false);
          window.dispatchEvent(new Event('storage')); // Trigger React to listen for changes
          console.log('Trying to clear login status');
          return Promise.reject(refreshError);
        }
      }
        
      return Promise.reject(error); // Reject the error if it's not a 401 or it can't be refreshed
    }
  );

  return apiInstance; // Return the configured Axios instance
};

// Create Axios instance for the Flight Service
const flightServiceApi = createAxiosInstance(API_URLS.FLIGHT_API_BASE_URL);
//const bookingServiceApi = createAxiosInstance(API_URLS.BOOKING_API_BASE_URL);
const bookingServiceApi = createAxiosInstance(API_URLS.BOOKING_API_BASE_URL);

const paymentServiceApi= createAxiosInstance(API_URLS.PAYMENT_API_BASE_URL);
// Export the instances for use across different services
export { flightServiceApi, bookingServiceApi , paymentServiceApi};
