import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Alert, Spinner, Form, Image, Button, Modal, Col, Row } from 'react-bootstrap';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import { styled } from '@mui/material/styles';
import { tableCellClasses } from '@mui/material/TableCell';
import EventService from '../services/EventService';
import VoucherService from '../services/VoucherService';

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

function EventDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [vouchers, setVouchers] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [voucherToDelete, setVoucherToDelete] = useState(null);

    useEffect(() => {
        const fetchEventDetail = async () => {
            try {
                const response = await EventService.getEventByEventId(id);
                setEvent(response.data);
                setLoading(false);
            } catch (error) {
                setError('Error fetching event details.');
                setLoading(false);
            }
        };

        fetchEventDetail();

        const fetchVouchers = async () => {
            try {
                const response = await VoucherService.getVoucherByEventId(id, page, rowsPerPage);
                setVouchers(response.data.content);
                setTotalElements(response.data.totalElements);
                setLoading(false);
            } catch (error) {
                setError('Error fetching vouchers.');
                setLoading(false);
            }
        };

        fetchVouchers();
    }, [id, page, rowsPerPage]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const handleViewDetailVoucher = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleEditVoucher = async (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    const handleDeleteVoucher = (voucherId) => {
        setVoucherToDelete(voucherId);
        setShowDeleteModal(true);
    };

    const handleEdit = () => {
        navigate(`/events/edit/${id}`);
    };

    const handleDelete = async () => {
        if (voucherToDelete) {
            try {
                await VoucherService.deleteVoucher(voucherToDelete);
                setVouchers(vouchers.filter(voucher => voucher.id !== voucherToDelete));
                setShowDeleteModal(false);
            } catch (error) {
                setError('Error deleting voucher.');
            }
        }
    };

    const handleBack = () => {
        navigate(-1);
    };
    
    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container className="mt-5">
            <h2 className="text-center">Event Detail</h2>
            <Row className="mt-5">
                <Col md={6}>
                    <Form>
                        <Form.Group controlId="formEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={event.name}
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formvoucherCount">
                            <Form.Label>Number of Vouchers</Form.Label>
                            <Form.Control
                                type="number"
                                value={event.voucherCount}
                                disabled
                            />
                        </Form.Group>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formStartTime">
                                <Form.Label>Start Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    value={event.startTime}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={5} controlId="formEndTime">
                                <Form.Label>End Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    value={event.endTime}
                                    disabled
                                />
                            </Form.Group>
                        </div>

                        <Form.Group>
                            <Form.Label>Game Type</Form.Label>
                            <Form.Control type="text" value={event.gameType.replace(';', ', ')} disabled />
                        </Form.Group>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" onClick={handleEdit}>
                                <i className="bi bi-pencil mr-2"></i> Edit
                            </Button>
                        </div>
                    </Form>
                </Col>
                <Col md={6}>
                <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                    >
                        {event?.image && (
                            <Image
                                src={event.image}
                                alt="Event Image"
                                thumbnail
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    objectFit: 'contain'
                                }}
                            />
                        )}
                    </div>
                </Col>
            </Row>
            <h3 className="mt-5">Vouchers</h3>
            {vouchers && vouchers.length > 0 ? (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <StyledTableCell>#</StyledTableCell>
                                <StyledTableCell>Code</StyledTableCell>
                                <StyledTableCell>QR Code</StyledTableCell>
                                <StyledTableCell>Image</StyledTableCell>
                                <StyledTableCell>Value</StyledTableCell>
                                <StyledTableCell>Description</StyledTableCell>
                                <StyledTableCell>Expiration Date</StyledTableCell>
                                <StyledTableCell>Status</StyledTableCell>
                                <StyledTableCell>Event Name</StyledTableCell>
                                <StyledTableCell>Action</StyledTableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {vouchers.map((voucher, index) => (
                                <StyledTableRow key={voucher.id}>
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
                                    <StyledTableCell>{voucher.value}</StyledTableCell>
                                    <StyledTableCell>
                                        {voucher.description.length > 50 ? `${voucher.description.slice(0, 50)}...` : voucher.description}
                                    </StyledTableCell>
                                    <StyledTableCell>{new Date(voucher.expirationDate).toLocaleString()}</StyledTableCell>
                                    <StyledTableCell>{voucher.status}</StyledTableCell>
                                    <StyledTableCell>{voucher.event.name}</StyledTableCell>
                                    <StyledTableCell>
                                        <div className="icon-container d-flex" style={{ gap: '10px' }}>
                                            <i
                                                className="bi bi-eye"
                                                style={{ color: 'blue', cursor: 'pointer' }}
                                                onClick={() => handleViewDetailVoucher(voucher.id)}
                                            ></i>
                                            <i
                                                className="bi bi-pencil"
                                                style={{ color: 'green', cursor: 'pointer' }}
                                                onClick={() => handleEditVoucher(voucher.id)}
                                            ></i>
                                            <i
                                                className="bi bi-trash3"
                                                style={{ color: 'red', cursor: 'pointer' }}
                                                onClick={() => handleDeleteVoucher(voucher.id)}
                                            ></i>
                                        </div>
                                    </StyledTableCell>
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25]}
                        component="div"
                        count={totalElements}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                    />
                </TableContainer>
            ) : (
                !loading && <Alert variant="info">No vouchers found for this event.</Alert>
            )}

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Delete Voucher</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this voucher?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default EventDetail;
