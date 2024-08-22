import React, { useState, useEffect } from 'react';
import { Table, Container, Alert, Spinner } from 'react-bootstrap';
import VoucherService from '../services/VoucherService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';

function VouchersTable({ brandID }) {
    const navigate = useNavigate();
    const [vouchers, setVouchers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredVouchers, setFilteredVouchers] = useState([]);

    useEffect(() => {
        const fetchVouchers = async () => {
            try {
                const response = await VoucherService.getVoucherByBrandId(brandID);
                console.log('response: ', response);
                setVouchers(response.data);
                setFilteredVouchers(response.data); // Initialize filteredVouchers with all vouchers
                setLoading(false);
            } catch (error) {
                setError('Error fetching vouchers.');
                setLoading(false);
            }
        };

        fetchVouchers();
    }, [brandID]);

    const handleSearch = () => {
        const results = vouchers.filter(voucher =>
            voucher.event.name.toLowerCase().trim() === searchQuery.toLowerCase().trim()
        );
        setFilteredVouchers(results);
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleViewDetails = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleDelete = async (voucherId) => {
        try {
            await VoucherService.deleteVoucher(voucherId);
            setVouchers(vouchers.filter(voucher => voucher.id !== voucherId));
            setFilteredVouchers(filteredVouchers.filter(voucher => voucher.id !== voucherId));
        } catch (error) {
            setError('Error deleting voucher.');
        }
    };

    const handleEdit = async (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    return (
        <Container className='mt-5'>
            <div className="search-bar mr-3 mb-4">
                <div className="input-group">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search by event name..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        onKeyDown={handleKeyDown} 
                    />
                    <button className="btn btn-outline-secondary" type="button" onClick={handleSearch}>
                        <FontAwesomeIcon icon={faSearch} />
                    </button>
                </div>
                <button className="btn btn-primary mt-3" onClick={() => window.location.href = `/add-voucher`}>
                    Add voucher
                </button>
            </div>
            {loading && <Spinner animation="border" />}
            {error && <Alert variant="danger">{error}</Alert>}
            <h2 className="text-center">Vouchers List</h2>
            {filteredVouchers.length > 0 ? (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>QR Code</th>
                            <th>Image</th>
                            <th>Value</th>
                            <th>Description</th>
                            <th>Expiration Date</th>
                            <th>Status</th>
                            <th>Event Name</th>
                            <th>Action</th> 
                        </tr>
                    </thead>
                    <tbody>
                        {filteredVouchers.map(voucher => (
                            <tr key={voucher.id}>
                                <td>{voucher.code}</td>
                                <td>
                                    <a href={voucher.qrcode} target="_blank" rel="noopener noreferrer">
                                        {voucher.qrcode.length > 30 ? `${voucher.qrcode.slice(0, 30)}...` : voucher.qrcode}
                                    </a>
                                </td>
                                <td>
                                    <a href={voucher.image} target='_blank' rel="nooperner noreferrer">
                                        {voucher.image.length > 30 ? `${voucher.image.slice(0, 30)}...` : voucher.image}
                                    </a>
                                </td>
                                <td>{voucher.value}</td>
                                <td>{voucher.description.length > 50 ? `${voucher.description.slice(0, 50)}...` : voucher.description}</td>
                                <td>{new Date(voucher.expirationDate).toLocaleString()}</td>
                                <td>{voucher.status}</td>
                                <td>{voucher.event.name}</td>
                                <td>
                                    <div
                                        className="icon-container d-flex"
                                        style={{
                                            justifyContent: 'space-between',
                                            alignItems: 'center',
                                            gap: '10px',
                                            padding: '5px',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        <i
                                            className="bi bi-eye"
                                            style={{ color: 'blue'}}
                                            onClick={() => handleViewDetails(voucher.id)}
                                        ></i>
                                        <i
                                            className="bi bi-pencil"
                                            style={{ color: 'green'}}
                                            onClick={() => handleEdit(voucher.id)}
                                        ></i>
                                        <i
                                            className="bi bi-trash3"
                                            style={{ color: 'red'}}
                                            onClick={() => handleDelete(voucher.id)}
                                        ></i>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            ) : (
                !loading && <Alert variant="info">No vouchers found for this event.</Alert>
            )}
        </Container>
    );
}

export default VouchersTable;