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

    getVoucherByBrandId(brandID, searchTerm, page = 0, size = 10) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/brand/${brandID}?pageNumber=${page}&pageSize=${size}&searchTerm=${searchTerm}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherByVoucherId(voucherID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/${voucherID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherByEventId(eventID, searchTerm, page = 0, size = 10) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/event/${eventID}?pageNumber=${page}&pageSize=${size}&searchTerm=${searchTerm}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    updateVoucher(voucher, voucherID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.put(`${VOUCHER_API_BASE_URL}/${voucherID}`, voucher, {
            headers: {
                'Content-Type': 'multipart/form-data',
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    deleteVoucher(voucherID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.delete(`${VOUCHER_API_BASE_URL}/${voucherID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherStatsGeneralByEvent(eventID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/statistics-general/event/${eventID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }

    getVoucherStatsDetailByEvent(eventID) {
        const token = JSON.parse(localStorage.getItem('token'));
        const accessToken = token.accessToken;

        return axios.get(`${VOUCHER_API_BASE_URL}/statistics-detail/event/${eventID}`, {
            headers: {
                'Authorization': `Bearer ${accessToken}}`
            },
            withCredentials: true
        });
    }
}

const voucherServiceInstance = new VoucherService();
export default voucherServiceInstance; 