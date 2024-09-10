import axios from 'axios';

const API_URL = "http://localhost:1110/api/v1";

export const getStatistics = (brandID) => {
    const token = JSON.parse(sessionStorage.getItem('token'));
    const accessToken = token.accessToken; // Lấy token từ localStorage
    return axios.get(`${API_URL}/budget-statistics`, {
        params: { brandID },
        headers: {
            'Authorization': `Bearer ${accessToken}` // Thêm token vào header
        }
    })
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching profit data:', error);
            throw error;
        });
};

export const getParticipants = (brandID) => {
    const token = JSON.parse(sessionStorage.getItem('token'));
    const accessToken = token.accessToken;
    return axios.get(`${API_URL}/participant-statistics`, {
        params: { brandID },
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching participant data:', error);
            throw error;
        });
};