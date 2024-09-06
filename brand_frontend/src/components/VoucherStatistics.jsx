import React, { useState, useEffect } from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';
import "./VoucherStatistics.css";
import {Image, Row} from "react-bootstrap";
import Select from "react-select";
import EventService from "../services/EventService";
import { useAuth } from "../AuthProvider";
import VoucherService from "../services/VoucherService";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

const VoucherStatistics = () => {
    const auth = useAuth();
    const brandID = auth.brand.id;

    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);

    const [error, setError] = useState(null);

    const [voucherData, setVoucherData] = useState({
        totalVoucherTypes: 0,
        totalVoucherCount: 0,
        totalExchangedVouchers: 0,
        totalUsedVouchers: 0,
        expiredVouchers: 0,
        unexpiredVouchers: 0,
        remainingVouchers: 0,
        voucherQuantities: [],
        remainingQuantities: [],
        exchangedQuantities: [],
        usedQuantities: [],
    });

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                console.log("brandID: ", brandID);

                const response = await EventService.getAllEventsByBrandId(brandID);
                const eventOptions = response.data.map(event => ({
                    value: event.id,
                    label: event.name
                }));
                setEvents(eventOptions);
            } catch (error) {
                setError('Error fetching events.');
            }
        };

        fetchEvents();
    }, [brandID]);

    useEffect(() => {
        const fetchData = async () => {
            if (!selectedEvent) return;

            console.log("selectedEvent", selectedEvent);

            try {
                const response = await VoucherService.getVoucherStatsGeneralByEvent(selectedEvent);

                console.log("response: ", response);

                const statsGeneralData = response.data;

                const response2 = await VoucherService.getVoucherStatsDetailByEvent(selectedEvent);

                console.log("response2: ", response2);

                const statsDetailData = response2.data;

                const data = {
                    totalVoucherTypes: statsGeneralData.totalVoucherTypes,
                    totalVoucherCount: statsGeneralData.totalVoucherCount,
                    totalExchangedVouchers: statsGeneralData.totalExchangedVouchers,
                    totalUsedVouchers: statsGeneralData.totalUsedVouchers,
                    expiredVouchers: statsGeneralData.expiredVouchers,
                    unexpiredVouchers: statsGeneralData.unexpiredVouchers,
                    remainingVouchers: statsGeneralData.remainingVouchers,
                    voucherQuantities: statsDetailData.map(stats => ({
                        id: stats.code,
                        quantity: stats.totalVouchers
                    })),
                    remainingQuantities: statsDetailData.map(stats => ({
                        id: stats.code,
                        remaining: stats.remainingVouchers
                    })),
                    exchangedQuantities: statsDetailData.map(stats => ({
                        id: stats.code,
                        exchanged: stats.exchangedVouchers
                    })),
                    usedQuantities: statsDetailData.map(stats => ({
                        id: stats.code,
                        used: stats.usedVouchers
                    })),
                };

                setVoucherData(data);

            } catch (error) {
                setError('Error fetching voucher statistics.');
            }
        };

        fetchData();

    }, [selectedEvent]);

    const handleEventChange = (selectedOption) => {
        setSelectedEvent(selectedOption ? selectedOption.value : null);
    };

    const getLabels = (data) => (data || []).map(item => item.id);
    const getTotalData = (data) => (data || []).map(item => item.quantity);
    const getRemainingData = (data) => (data || []).map(item => item.remaining);
    const getExchangedData = (data) => (data || []).map(item => item.exchanged);
    const getUsedData = (data) => (data || []).map(item => item.used);

    const voucherChartData = {
        labels: getLabels(voucherData.voucherQuantities),
        datasets: [
            {
                label: 'Total Quantity',
                data: getTotalData(voucherData.voucherQuantities),
                backgroundColor: 'rgb(132,217,210, 0.2)',
                borderColor: 'rgba(7,205,174, 1)',
                borderWidth: 1,
            },
            {
                label: 'Remaining Quantity',
                data: getRemainingData(voucherData.remainingQuantities),
                backgroundColor: 'rgba(255, 159, 64, 0.2)',
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 1,
            }
        ]
    };

    const voucherExchangedChartData = {
        labels: getLabels(voucherData.voucherQuantities),
        datasets: [
            {
                label: 'Exchanged Quantity',
                data: getExchangedData(voucherData.exchangedQuantities),
                backgroundColor: 'rgba(144,202,249, 0.2)',
                borderColor: 'rgba(4,126,223, 1)',
                borderWidth: 1,
            },
            {
                label: 'Used Quantity',
                data: getUsedData(voucherData.usedQuantities),
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1,
            }
        ]
    };

    const pieChartExpiredVoucherData = {
        labels: ['Expired', 'Active'],
        datasets: [{
            label: 'Quantity',
            data: [voucherData.expiredVouchers, voucherData.unexpiredVouchers],
            backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgb(132,217,210, 0.2)'],
            borderColor: ['rgba(255, 99, 132, 1)', 'rgba(7,205,174, 1)'],
            borderWidth: 1
        }]
    };

    const pieChartExchangedVoucherData = {
        labels: ['Exchanged', 'Remaining'],
        datasets: [{
            label: 'Quantity',
            data: [voucherData.totalExchangedVouchers, voucherData.remainingVouchers],
            backgroundColor: ['rgba(144,202,249, 0.2)', 'rgba(255, 159, 64, 0.2)'],
            borderColor: ['rgba(4,126,223, 1)', 'rgba(255, 159, 64, 1)'],
            borderWidth: 1
        }]
    };

    const pieChartUsedVoucherData = {
        labels: ['Unused', 'Used'],
        datasets: [{
            label: 'Quantity',
            data: [voucherData.totalVoucherCount - voucherData.totalUsedVouchers, voucherData.totalUsedVouchers],
            backgroundColor: ['rgba(246,227,132, 0.2)', 'rgba(153, 102, 255, 0.2)'],
            borderColor: ['rgba(255,213,0, 1)', 'rgba(153, 102, 255, 1)'],
            borderWidth: 1
        }]
    };

    const options = {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    };

    const imageVoucherTypePath = '/voucher.png';
    const imageVoucherPath = '/vouchers.png';
    const imageExchangedVoucherPath = '/gift-voucher.png';
    const imageUsedVoucherPath = '/used_voucher.png';

    return (
        <div>
            <div className="mt-5">
                <div className="title">Filter:</div>
                <Select
                    name="event"
                    options={ events }
                    className="basic-single"
                    classNamePrefix="select"
                    onChange={ handleEventChange }
                    placeholder="Select an event"
                />
            </div>

            {error && <div className="alert alert-danger mt-5">{error}</div>}

            <h1 className="text-center mt-5">Voucher Statistics</h1>

            <Row className="d-flex justify-content-between mt-5">
                <div className="sales-card">
                    <p className="title">Total Voucher Types</p>
                    <div className="d-flex align-items-center">
                        <Image src={ imageVoucherTypePath } alt="voucher" style={{width: '50px', height: '50px', marginRight: '12px'}} />
                        <p className="amount">{ voucherData.totalVoucherTypes }</p>
                    </div>
                </div>

                <div className="sales-card success-background-gradient">
                    <p className="title">Total Voucher</p>
                    <div className="d-flex align-items-center">
                        <Image src={ imageVoucherPath } alt="voucher" style={{width: '50px', height: '50px', marginRight: '12px'}} />
                        <p className="amount">{ voucherData.totalVoucherCount }</p>
                    </div>
                </div>

                <div className="sales-card info-background-gradient">
                    <p className="title">Total Exchanged Voucher</p>
                    <div className="d-flex align-items-center">
                        <Image src={ imageExchangedVoucherPath } alt="voucher" style={{width: '50px', height: '50px', marginRight: '12px'}} />
                        <p className="amount">{ voucherData.totalExchangedVouchers }</p>
                    </div>
                </div>

                <div className="sales-card purple-background-gradient">
                    <p className="title">Total Used Voucher</p>
                    <div className="d-flex align-items-center">
                        <Image src={ imageUsedVoucherPath } alt="voucher" style={{width: '50px', height: '50px', marginRight: '12px'}} />
                        <p className="amount">{ voucherData.totalUsedVouchers }</p>
                    </div>
                </div>
            </Row>

            <div className="d-flex justify-content-between mt-5">
                <div className="text-center">
                    <h3>Status Of Vouchers</h3>
                    <Pie key="voucher-pie" data={ pieChartExpiredVoucherData } className="pie-chart-container" />
                </div>

                <div className="text-center">
                    <h3>Exchanged Vouchers</h3>
                    <Pie key="voucher-pie" data={ pieChartExchangedVoucherData } className="pie-chart-container" />
                </div>

                <div className="text-center">
                    <h3>Used Vouchers</h3>
                    <Pie key="voucher-pie" data={ pieChartUsedVoucherData } className="pie-chart-container" />
                </div>
            </div>

            <div className="stat-box mt-5">
                <h3>Total Voucher vs Remaining Voucher</h3>
                <Bar key="voucher-bar" data={ voucherChartData } options={options} />
            </div>

            <div className="stat-box mt-5">
                <h3>Voucher Exchanged vs Used</h3>
                <Bar key="voucher-bar" data={ voucherExchangedChartData } options={options} />
            </div>

        </div>
    );
};

export default VoucherStatistics;
