import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// REQUEST INTERCEPTOR (Attaches the VIP Badge)
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// RESPONSE INTERCEPTOR (The Global Error Catcher)
api.interceptors.response.use(
    (response) => {
        // If the request was successful, just pass the data through normally
        return response;
    },
    (error) => {
        // If the backend threw an error, catch it here!
        if (error.response) {
            const status = error.response.status;

            // If the token is expired or invalid (401 or 403)
            if (status === 401 || status === 403) {
                console.warn("Session expired or unauthorized. Logging out.");
                localStorage.removeItem('token');
                window.location.href = '/login'; // Force redirect to login
            }

            // For other errors, we can log the clean message we sent from Spring Boot
            if (error.response.data && error.response.data.message) {
                console.error("Backend Error:", error.response.data.message);
            }
        }
        return Promise.reject(error);
    }
);

export default api;