import axios from 'axios';

const VOUCHER_API_BASE_URL = "http://localhost:1110/api/v1/events/vouchers";

class VoucherService {
    createVoucher(voucher) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.post(`${VOUCHER_API_BASE_URL}`, voucher, {
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherByBrandId(brandId, searchTerm, page = 0, size = 10) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/brand/${brandId}?pageNumber=${page}&pageSize=${size}&searchTerm=${searchTerm}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherByVoucherId(voucherId) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/${voucherId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherByEventId(eventId, searchTerm, page = 0, size = 10) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/event/${eventId}?pageNumber=${page}&pageSize=${size}&searchTerm=${searchTerm}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    updateVoucher(voucher, voucherId) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.put(`${VOUCHER_API_BASE_URL}/${voucherId}`, voucher, {
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    deleteVoucher(voucherId) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.delete(`${VOUCHER_API_BASE_URL}/${voucherId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherStatsGeneralByEvent(eventId) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/statistics-general/event/${eventId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherStatsDetailByEvent(eventId) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/statistics-detail/event/${eventId}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const voucherServiceInstance = new VoucherService();
export default voucherServiceInstance; 