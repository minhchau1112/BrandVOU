import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Container,
    Alert,
    Spinner,
    Form,
    Image,
    Col,
    Row,
    FormControl,
    InputGroup, Pagination
} from 'react-bootstrap';
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
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSearch} from "@fortawesome/free-solid-svg-icons";
import EventGamesService from "../services/EventGamesService";
import GameService from "../services/GameService";
import Select from "react-select";
import ItemCard from "./ItemCard";
import itemService from "../services/ItemService";
import TextField from "@mui/material/TextField";
import quizService from "../services/QuizSerivce";
import Button from '@mui/material/Button';

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
    { id: 'index', label: '#', minWidth: 50 },
    { id: 'question', label: 'Question', minWidth: 190 },
    { id: 'correctAnswer', label: 'Correct Answer', minWidth: 190 },
    { id: 'wrongAnswer1', label: 'Wrong Answer 1', minWidth: 190 },
    { id: 'wrongAnswer2', label: 'Wrong Answer 2', minWidth: 190 },
    { id: 'wrongAnswer3', label: 'Wrong Answer 3', minWidth: 190 },
];

function EventDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [vouchers, setVouchers] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [pageNumber, setPageNumber] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [games, setGames] = useState([]);
    const [seletedOptions, setSelectedOptions] = useState([]);
    const [items, setItems] = useState([]);
    const [pageNumberItem, setPageNumberItem] = useState(0);
    const [pageSizeItem] = useState(12);
    const [totalElementsItem, setTotalElementsItem] = useState(0);

    const [rows, setRows] = useState([{ question: '', correctAnswer: '', wrongAnswer1: '', wrongAnswer2: '', wrongAnswer3: '' }]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const fetchVouchers = useCallback(async () => {
        try {
            const response = await VoucherService.getVoucherByEventId(id, searchTerm, pageNumber, pageSize);
            setVouchers(response.data.content);
            setTotalElements(response.data.totalElements);
            setLoading(false);
        } catch (error) {
            setError('Error fetching vouchers.');
            setLoading(false);
        }
    }, [id, searchTerm, pageNumber, pageSize]);

    const fetchItems = useCallback(async () => {
        try {
            if (Array.isArray(seletedOptions) && seletedOptions.some(game => game.isItemExchangeAllowed === true)) {
                const items = await itemService.getItemsByEventId(id, pageNumberItem, pageSizeItem);
                console.log("items: ", items);
                setItems(items.data.content);
                setTotalElementsItem(items.data.totalElements);
            }
        } catch (error) {
            setError('Error fetching item.');
            setLoading(false);
        }
    }, [id, seletedOptions, pageNumberItem, pageSizeItem]);

    const fetchQuiz = useCallback(async () => {
        try {
            if (Array.isArray(seletedOptions)) {
                const allowedGames = seletedOptions.filter(game => game.isItemExchangeAllowed === false);
                console.log("allowedGames: ", allowedGames);

                if (allowedGames.length > 0) {
                    console.log("allowedGames: ", allowedGames);

                    const quizs = await quizService.getRowQuizQuestionResponse(id, allowedGames[0].value);

                    console.log("quizs: ", quizs);

                    setRows(quizs.data);
                }
            }
        } catch (error) {
            setError('Error fetching item.');
        } finally {
            setLoading(false);
        }
    }, [id, seletedOptions]);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const [gamesResponse, eventResponse] = await Promise.all([
                    GameService.getAllGames(),
                    EventService.getEventByEventId(id)
                ]);

                const gameOptions = gamesResponse.data.map(game => ({
                    value: game.id,
                    label: game.name,
                    isItemExchangeAllowed: game.isItemExchangeAllowed
                }));
                setGames(gameOptions);

                const eventData = eventResponse.data;
                setEvent(eventData);

                const gamesNameString = await EventGamesService.findGameNamesByEventId(eventData.id);
                const gameNames = gamesNameString.data.split(";");

                const selectedGames = gameOptions.filter(game => gameNames.includes(game.label));
                setSelectedOptions(selectedGames);
                setLoading(false);
            } catch (error) {
                setError('Error fetching data.');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [id]);

    useEffect(() => {
        fetchVouchers();
    }, [fetchVouchers]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    useEffect(() => {
        fetchQuiz();
    }, [fetchQuiz]);

    const handleSearch = () => {
        setPageNumber(0);
        fetchVouchers();
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleChangePage = (event, newPage) => {
        setPageNumber(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(+event.target.value);
        setPageNumber(0);
    };

    const handleChangePageItem = (item, newPage) => {
        setPageNumberItem(newPage);
    };

    const handleViewDetailVoucher = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleEdit = () => {
        navigate(`/events/edit/${id}`);
    };

    const handleBack = () => {
        navigate(-1);
    };

    const handleChangePageQuizQuestion = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPageQuizQuestion = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const handleInputChangeQuizQuestion = (index, field, value) => {
        const newRows = [...rows];
        newRows[index][field] = value;
        setRows(newRows);
    };

    const handleAddRowQuizQuestion = () => {
        setRows([...rows, { question: '', correctAnswer: '', wrongAnswer1: '', wrongAnswer2: '', wrongAnswer3: '' }]);
    };

    const handleDeleteRowQuizQuestion = (rowIndex) => {
        const newRows = rows.filter((_, index) => index !== rowIndex);
        setRows(newRows);
    };
    const pageCount = totalElementsItem > 0 ? Math.ceil(totalElementsItem / pageSizeItem) : 1;

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
                                value={event?.name}
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formvoucherCount">
                            <Form.Label>Number of Vouchers</Form.Label>
                            <Form.Control
                                type="number"
                                value={event?.voucherCount}
                                disabled
                            />
                        </Form.Group>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formStartTime">
                                <Form.Label>Start Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    value={event?.startTime}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={5} controlId="formEndTime">
                                <Form.Label>End Time</Form.Label>
                                <Form.Control
                                    type="datetime-local"
                                    value={event?.endTime}
                                    disabled
                                />
                            </Form.Group>
                        </div>

                        <Form.Group>
                            <Form.Label>Games</Form.Label>
                            <Select
                                isMulti
                                options={games}
                                className="basic-multi-select"
                                classNamePrefix="select"
                                value={seletedOptions}
                                isDisabled={true}
                            />
                        </Form.Group>

                        {Array.isArray(seletedOptions) && seletedOptions.some(game => game.isItemExchangeAllowed === true) && (
                            <Form.Group>
                                <Form.Label>Target Word</Form.Label>
                                <Form.Control
                                    type="text"
                                    value={event.targetWord}
                                    disabled
                                />
                            </Form.Group>
                        )}
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

            <h3 className="text-center mt-3">Vouchers</h3>
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
                                    <StyledTableCell>{voucher.value}</StyledTableCell>
                                    <StyledTableCell>
                                        {voucher.description.length > 50 ? `${voucher.description.slice(0, 50)}...` : voucher.description}
                                    </StyledTableCell>
                                    <StyledTableCell>{new Date(voucher.expirationDate).toLocaleString()}</StyledTableCell>
                                    <StyledTableCell>{voucher.status}</StyledTableCell>
                                    <StyledTableCell>{voucher.event.name}</StyledTableCell>
                                    <StyledTableCell>
                                        <div className="icon-container justify-content-center d-flex" style={{ gap: '10px' }}>
                                            <i
                                                className="bi bi-eye"
                                                style={{ color: 'blue', cursor: 'pointer' }}
                                                onClick={() => handleViewDetailVoucher(voucher.id)}
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
            ) : (
                !loading && <Alert variant="info">No vouchers found for this event.</Alert>
            )}

            {Array.isArray(seletedOptions) && seletedOptions.some(game => game.isItemExchangeAllowed === true) ? (
                <div>
                    <h2 className="text-center">Items List</h2>
                    <div className="event-list">
                        {items.length > 0 ? (
                            items.map(item => (
                                <ItemCard key={item.id} item={item} />
                            ))
                        ) : (
                            <div>
                                <Alert variant="info" className="mt-5">No items found for this event.</Alert>
                            </div>
                        )}
                    </div>
                    <div className="pagination mt-4">
                        <Pagination>
                            <Pagination.Prev onClick={() => handleChangePageItem(pageNumber > 0 ? pageNumber - 1 : 0)} />
                            {[...Array(pageCount).keys()].map(number => (
                                <Pagination.Item
                                    key={number}
                                    active={number === pageNumberItem}
                                    onClick={() => handleChangePageItem(number)}
                                >
                                    {number + 1}
                                </Pagination.Item>
                            ))}
                            <Pagination.Next onClick={() => handleChangePageItem(pageNumberItem < pageCount - 1 ? pageNumber + 1 : pageCount - 1)} />
                        </Pagination>
                    </div>
                </div>
            ) : (<div></div>)}

            {Array.isArray(seletedOptions) && seletedOptions.some(game => game.isItemExchangeAllowed === false) ? (
                <Paper className="mt-4" sx={{ width: '100%', overflow: 'hidden', padding: 2 }}>
                    <h3>List Question for Quiz Game</h3>
                    <TableContainer sx={{ maxHeight: 440 }}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    {columns.map((column) => (
                                        <TableCell
                                            key={column.id}
                                            style={{ minWidth: column.minWidth }}
                                        >
                                            {column.label}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, rowIndex) => (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={rowIndex}>
                                        {/* Serial number column */}
                                        <TableCell>{rowIndex + 1 + page * rowsPerPage}</TableCell>
                                        {columns.slice(1, 6).map((column) => (
                                            <TableCell key={column.id}>
                                                <TextField
                                                    fullWidth
                                                    variant="outlined"
                                                    value={row[column.id]}
                                                    onChange={(e) => handleInputChangeQuizQuestion(rowIndex, column.id, e.target.value)}
                                                />
                                            </TableCell>
                                        ))}
                                    </TableRow>
                                ))}
                            </TableBody>

                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[10, 25, 100]}
                        component="div"
                        count={rows.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePageQuizQuestion}
                        onRowsPerPageChange={handleChangeRowsPerPageQuizQuestion}
                    />
                </Paper>
            ) : (<div></div>)}

            <div className="d-flex justify-content-start mt-4 mb-5" style={{gap: '12px'}}>
                <Button variant="contained" color="inherit" onClick={handleBack}>
                    <i className="bi bi-arrow-left mr-2"></i> Back
                </Button>

                <Button variant="contained" color="primary" onClick={handleEdit}>
                    <i className="bi bi-pencil mr-2"></i> Edit
                </Button>
            </div>
        </Container>
    );
}

export default EventDetail;
