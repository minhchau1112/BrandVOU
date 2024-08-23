import axios from 'axios';

const VOUCHER_API_BASE_URL = "http://localhost:9090/api/v1/vouchers";

class VoucherService {
    createVoucher(voucher, eventId) {
        return axios.post(`${VOUCHER_API_BASE_URL}/${eventId}`, voucher, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    getVoucherByBrandId(brandId, page = 0, size = 10) {
        return axios.get(`${VOUCHER_API_BASE_URL}/${brandId}?page=${page}&size=${size}`);
    }

    getVoucherByVoucherId(voucherId) {
        return axios.get(`${VOUCHER_API_BASE_URL}/view-detail/${voucherId}`);
    }

    getVoucherByEventId(eventId) {
        return axios.get(`${VOUCHER_API_BASE_URL}/event/${eventId}`);
    }

    updateVoucher(voucher, voucherId) {
        return axios.put(`${VOUCHER_API_BASE_URL}/update/${voucherId}`, voucher, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
        });
    }

    deleteVoucher(voucherId) {
        return axios.delete(`${VOUCHER_API_BASE_URL}/delete/${voucherId}`);
    }

    checkDuplicate(code, eventId) {
        return axios.get(`${VOUCHER_API_BASE_URL}/check-duplicate?code=${code}&eventId=${eventId}`);
    }
}

const voucherServiceInstance = new VoucherService();
export default voucherServiceInstance; 