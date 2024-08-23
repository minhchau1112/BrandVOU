import React, { useState, useEffect } from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import { styled } from '@mui/material/styles';
import { tableCellClasses } from '@mui/material/TableCell';
import VoucherService from '../services/VoucherService';
import { useNavigate } from 'react-router-dom';
import { Alert, Spinner } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: '#008ecf',
        color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
        fontSize: 14,
    },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
    '&:last-child td, &:last-child th': {
        border: 0,
    },
}));

const columns = [
    { id: 'index', label: '#', width: 50 },
    { id: 'code', label: 'Code', width: 100 },
    { id: 'qrcode', label: 'QR Code', width: 20 },
    { id: 'image', label: 'Image', width: 20 },
    { id: 'value', label: 'Value', width: 50, align: 'right' },
    { id: 'description', label: 'Description', width: 50 },
    { id: 'expirationDate', label: 'Expiration Date', width: 180 },
    { id: 'status', label: 'Status', width: 50 },
    { id: 'count', label: 'Count', width: 10},
    { id: 'eventName', label: 'Event Name', width: 100 },
    { id: 'action', label: 'Action', width: 50, align: 'center' },
];

function VouchersTable({ brandID }) {
    const navigate = useNavigate();
    const [vouchers, setVouchers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredVouchers, setFilteredVouchers] = useState([]);
    const [page, setPage] = useState(0); 
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalElements, setTotalElements] = useState(0);

    useEffect(() => {
        const fetchVouchers = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await VoucherService.getVoucherByBrandId(brandID, page, rowsPerPage);
                setVouchers(response.data.content);
                setFilteredVouchers(response.data.content);
                setTotalElements(response.data.totalElements);
                setLoading(false);
            } catch (error) {
                setError('Error fetching vouchers.');
                setLoading(false);
            }
        };

        fetchVouchers();
    }, [brandID, page, rowsPerPage]);

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

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
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

    const handleEdit = (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    return (
        <>
            <div className="search-bar mr-3 mb-4 mt-5">
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
                <button className="btn btn-primary mt-3" onClick={() => navigate('/add-voucher')}>
                    Add voucher
                </button>
            </div>
            <h2 className="text-center">Vouchers List</h2>

            <Paper sx={{ width: '100%', overflow: 'hidden' }} className='mt-5'>
                {loading && <Spinner animation="border" />}
                {error && <Alert variant="danger">{error}</Alert>}
                {filteredVouchers.length > 0 ? (
                    <TableContainer sx={{ maxHeight: 550 }}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    {columns.map((column) => (
                                        <StyledTableCell
                                            key={column.id}
                                            align={column.align}
                                            style={{ minWidth: column.width }}
                                        >
                                            {column.label}
                                        </StyledTableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {filteredVouchers
                                    .map((voucher, index) => (
                                        <StyledTableRow hover role="checkbox" tabIndex={-1} key={voucher.id}>
                                            <StyledTableCell>{page * rowsPerPage + index + 1}</StyledTableCell>
                                            <StyledTableCell>{voucher.code}</StyledTableCell>
                                            <StyledTableCell>
                                                <a href={voucher.qrcode} target="_blank" rel="noopener noreferrer">
                                                    {voucher.qrcode.length > 20 ? `${voucher.qrcode.slice(0, 20)}...` : voucher.qrcode}
                                                </a>
                                            </StyledTableCell>
                                            <StyledTableCell>
                                                <a href={voucher.image} target='_blank' rel="noopener noreferrer">
                                                    {voucher.image.length > 20 ? `${voucher.image.slice(0, 20)}...` : voucher.image}
                                                </a>
                                            </StyledTableCell>
                                            <StyledTableCell align="right">{voucher.value}</StyledTableCell>
                                            <StyledTableCell>{voucher.description.length > 50 ? `${voucher.description.slice(0, 50)}...` : voucher.description}</StyledTableCell>
                                            <StyledTableCell>{new Date(voucher.expirationDate).toLocaleString()}</StyledTableCell>
                                            <StyledTableCell>{voucher.status}</StyledTableCell>
                                            <StyledTableCell>{voucher.count}</StyledTableCell>
                                            <StyledTableCell>{voucher.event.name}</StyledTableCell>
                                            <StyledTableCell>
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
                                                        style={{ color: 'blue' }}
                                                        onClick={() => handleViewDetails(voucher.id)}
                                                    ></i>
                                                    <i
                                                        className="bi bi-pencil"
                                                        style={{ color: 'green' }}
                                                        onClick={() => handleEdit(voucher.id)}
                                                    ></i>
                                                    <i
                                                        className="bi bi-trash3"
                                                        style={{ color: 'red' }}
                                                        onClick={() => handleDelete(voucher.id)}
                                                    ></i>
                                                </div>
                                            </StyledTableCell>
                                        </StyledTableRow>
                                    ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                ) : (
                    !loading && <Alert variant="info">No vouchers found for this event.</Alert>
                )}
                <TablePagination
                    rowsPerPageOptions={[10, 25, 100]}
                    component="div"
                    count={totalElements}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </Paper>
        </>
    );
}

export default VouchersTable;