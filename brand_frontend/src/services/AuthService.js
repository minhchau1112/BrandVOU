import axios from 'axios';

const API_URL = "http://localhost:9090/api/auth/";

const register = (username, password, email, phoneNumber, name, field, address) => {
    return axios.post(API_URL + "register", {
        username,
        password,
        email,
        phoneNumber,
        name,
        field,
        address
    });
};

const login = async (username, password) => {
    try {
        const response = await axios.post(API_URL + "login", {
            username,
            password
        });

        console.log('Login response:', response.data); // Log response data

        if (response.data) {
            localStorage.setItem('brandName', response.data.brandName); // Store brand name
            localStorage.setItem('brandId', response.data.brandId); // Store brand ID
            localStorage.setItem('token', response.data.token);
        }
        return response.data;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
};

export default {
    register,
    login,
};
