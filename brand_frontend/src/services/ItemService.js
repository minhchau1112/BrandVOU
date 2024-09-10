import axios from 'axios';

const ITEM_API_BASE_URL = "http://localhost:1110/api/v1/events/items";

class ItemService {
    createItem(item) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.post(`${ITEM_API_BASE_URL}`, item,{
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
    getItemsByBrandId(brandId, searchTerm, page = 0, size = 10) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${ITEM_API_BASE_URL}/brand/${brandId}?pageNumber=${page}&pageSize=${size}&searchTerm=${searchTerm}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    updateItem(itemId, item) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.put(`${ITEM_API_BASE_URL}/${itemId}`, item,{
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getItemByItemId(itemId) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${ITEM_API_BASE_URL}/${itemId}`,{
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getItemsByEventId(eventId, page = 0, size = 10) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${ITEM_API_BASE_URL}/event/${eventId}?pageNumber=${page}&pageSize=${size}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    deleteItem(itemId) {
        const token = JSON.parse(sessionStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.delete(`${ITEM_API_BASE_URL}/${itemId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const itemServiceInstance = new ItemService();

export default itemServiceInstance;