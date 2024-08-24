import axios from 'axios';

const API_URL = "http://localhost:9090/api/auth/";

const register = (username, password, email, phoneNumber) => {
    return axios.post(API_URL + "register", {
        username,
        password,
        email,
        phoneNumber
    });
};

const login = async (username, password) => {
    const response = await axios.post(API_URL + "login", {
        username,
        password
    });

    if(response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
};

export default {
    register,
    login,
};
