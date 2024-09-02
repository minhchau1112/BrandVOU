import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Form,
    Container,
    Button,
    Spinner,
    Alert,
    Image,
    Row,
    Col,
    Modal,
    InputGroup,
    FormControl
} from 'react-bootstrap';
import Select from 'react-select';
import EventService from '../services/EventService';
import VoucherService from '../services/VoucherService';
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
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSearch } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../AuthProvider";

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

function EditEvent() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState({
        name: '',
        image: null,
        startTime: '',
        endTime: '',
        gameType: '',
        voucherCount: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [previewImage, setPreviewImage] = useState(null);
    const [vouchers, setVouchers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [pageNumber, setPageNumber] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [voucherToDelete, setVoucherToDelete] = useState(null);

    const gameOptions = [
        { value: 'Quiz', label: 'Quiz' },
        { value: 'ShakeGame', label: 'ShakeGame' }
    ];

    const auth = useAuth();
    const fetchEventDetail = async () => {
        try {
            const response = await EventService.getEventByEventId(id);
            const eventData = response.data;

            const selectedGameTypes = eventData.gameType
                ? eventData.gameType.split(';').map(type => ({
                    value: type,
                    label: type
                }))
                : [];

            setEvent({
                ...eventData,
                gameType: selectedGameTypes
            });
            setPreviewImage(eventData.image);
            setLoading(false);
        } catch (error) {
            setError('Error fetching event details.');
            setLoading(false);
        }
    };

    const fetchVouchers = async () => {
        try {
            const response = await VoucherService.getVoucherByEventId(id, searchTerm, pageNumber, pageSize);
            console.log('voucher: ', response);
            setVouchers(response.data.content);
            setTotalElements(response.data.totalElements);
            setLoading(false);
        } catch (error) {
            setError('Error fetching vouchers.');
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchEventDetail();

        fetchVouchers();
    }, [id, pageNumber, pageSize, searchTerm]);

    const handleSearch = () => {
        setPageNumber(0); // Reset to the first page on new search
        fetchVouchers();
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        if (type === 'file') {
            const file = files[0];
            setEvent({ ...event, [name]: file });
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewImage(reader.result);
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            setEvent({ ...event, [name]: value });
        }
    };

    const handleGameTypeChange = (selectedOptions) => {
        setEvent({ ...event, gameType: selectedOptions });
    };

    const handleViewDetailVoucher = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleEditVoucher = async (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    const handleBack = () => {
        navigate(-1);
    };

    const handleDeleteVoucher = (voucherId) => {
        setVoucherToDelete(voucherId);
        setShowDeleteModal(true);
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

    async function createFileFromUrl(imageUrl) {
        try {
            const response = await fetch(imageUrl);

            const blob = await response.blob();

            const imageName = imageUrl.split('/').pop() || 'image.jpg';

            const file = new File([blob], imageName, { type: blob.type });

            return file;
        } catch (error) {
            console.error('Error creating file from URL:', error);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();
            formData.append('brandId', auth.brand.id);
            formData.append('name', event.name);

            if (typeof event.image === 'string') {
                const file = await createFileFromUrl(event.image);
                formData.append('image', file);
            } else {
                formData.append('image', event.image);
            }

            formData.append('voucherCount', event.voucherCount);
            formData.append('startTime', event.startTime);
            formData.append('endTime', event.endTime);
            formData.append('gameType', event.gameType.map(option => option.value).join(';'));

            await EventService.updateEvent(id, formData);
            navigate(`/events/view-detail/${id}`);
        } catch (error) {
            setError('Error updating event.');
        }
    };

    const handleChangePage = (event, newPage) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(+event.target.value);
        setPageNumber(0);
    };

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

    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container className="mt-5">
            <h2 className="text-center">Edit Event</h2>
            <Row className="mt-5">
                <Col md={6}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={event.name}
                                onChange={handleChange}
                                placeholder="Enter event name"
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formvoucherCount">
                            <Form.Label>Number of Vouchers</Form.Label>
                            <Form.Control
                                type="number"
                                name="voucherCount"
                                value={event.voucherCount}
                                onChange={handleChange}
                                placeholder="Enter number of vouchers"
                                required
                            />
                        </Form.Group>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formStartTime">
                                <Form.Label>Start Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    name="startTime"
                                    value={event.startTime}
                                    onChange={handleChange}
                                    required
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={5} controlId="formEndTime">
                                <Form.Label>End Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    name="endTime"
                                    value={event.endTime}
                                    onChange={handleChange}
                                    required
                                />
                            </Form.Group>
                        </div>

                        <Form.Group controlId="formGameType">
                            <Form.Label>Game Type</Form.Label>
                            <Select
                                isMulti
                                name="gameType"
                                options={gameOptions}
                                className="basic-multi-select"
                                classNamePrefix="select"
                                value={event.gameType}
                                onChange={handleGameTypeChange}
                                required
                            />
                        </Form.Group>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" type="submit">
                                Save Changes
                            </Button>
                        </div>
                    </Form>
                </Col>
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => document.querySelector('input[name="image"]').click()}
                    >
                        {previewImage && (
                            <Image
                                src={previewImage}
                                alt="Event Image Preview"
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    objectFit: 'contain'
                                }}
                            />
                        )}
                        <div className="overlay">
                            <div className="overlay-text">Add Image +</div>
                        </div>
                    </div>
                    <Form.Group controlId="formFile" className="d-none">
                        <Form.Control
                            type="file"
                            name="image"
                            onChange={handleChange}
                            accept="image/*"
                        />
                    </Form.Group>
                </Col>
            </Row>

            <InputGroup className="search-bar mt-5">
                <FormControl
                    placeholder="Search voucher by code ..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    onKeyDown={handleKeyDown}
                />
                <Button variant="outline-secondary" onClick={handleSearch}>
                    <FontAwesomeIcon icon={faSearch} />
                </Button>
            </InputGroup>

            <h3 className="mt-3">Vouchers</h3>
            {vouchers && vouchers.length > 0 ? (
                <TableContainer component={Paper}>
                <Table>
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
                        {vouchers.map((voucher, index) => (
                            <StyledTableRow hover role="checkbox" tabIndex={-1} key={voucher.id}>
                                <StyledTableCell>{pageNumber * pageSize + index + 1}</StyledTableCell>
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
                                            onClick={() => handleViewDetailVoucher(voucher.id)}
                                        ></i>
                                        <i
                                            className="bi bi-pencil"
                                            style={{ color: 'green' }}
                                            onClick={() => handleEditVoucher(voucher.id)}
                                        ></i>
                                        <i
                                            className="bi bi-trash3"
                                            style={{ color: 'red' }}
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
                    rowsPerPage={pageSize}
                    page={pageNumber}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </TableContainer>
            ): (
                !loading && <Alert variant="info">No vouchers found for this event.</Alert>
            )}

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Delete</Modal.Title>
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

export default EditEvent;