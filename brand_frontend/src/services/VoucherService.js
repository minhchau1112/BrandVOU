import axios from 'axios';

const VOUCHER_API_BASE_URL = "http://localhost:9090/api/v1/vouchers";

class VoucherService {

    getVoucher(){
        return axios.get(VOUCHER_API_BASE_URL);
    }

    createVoucher(voucher, eventID){
        return axios.post(`${VOUCHER_API_BASE_URL}/${eventID}`, voucher, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }

    getVoucherById(voucherId){
        return axios.get(VOUCHER_API_BASE_URL + '/' + voucherId);
    }

    updateVoucher(voucher, voucherId){
        return axios.put(VOUCHER_API_BASE_URL + '/' + voucherId, voucher);
    }

    deleteVoucher(voucherId){
        return axios.delete(VOUCHER_API_BASE_URL + '/' + voucherId);
    }
}

export default new VoucherService()