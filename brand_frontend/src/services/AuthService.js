import axios from 'axios';

const API_URL = "http://localhost:1110/api/v1";
const config = {
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
};

class AuthService {
    registerBrand(requestBody) {
        return axios.post(`${API_URL}/accounts/register-brand`, requestBody, config);
    }

    login(requestBody) {
        return axios.post(`${API_URL}/accounts/login-brand`, requestBody, config);
    }

    logout(requestBody) {
        return axios.post(`${API_URL}/authentication/accounts/logout`, requestBody, config);
    }

    refreshToken(requestBody) {
        return axios.post(`${API_URL}/authentication/accounts/refresh-token`, requestBody, config);
    }
}

const authServiceInstance = new AuthService();

export default authServiceInstance;
