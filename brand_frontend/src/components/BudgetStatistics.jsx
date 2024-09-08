import React, { useEffect, useState } from 'react';
import { getStatistics } from '../services/StatisticsService'; // Import service function
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const BudgetStatistics = () => {
    const [budgetData, setBudgetData] = useState([]);

    useEffect(() => {
        const brand = JSON.parse(localStorage.getItem('brand'));
        const brandID = brand?.id;

        if (brandID) {
            getStatistics(brandID)  // Use the service function
                .then(data => setBudgetData(data))
                .catch(error => console.error('Error fetching profit data:', error));
        }
    }, []);

    const chartData = {
        labels: budgetData.map(item => item.eventName), // X-axis labels
        datasets: [
            {
                label: 'Budget',
                data: budgetData.map(item => item.budget), // Y-axis data
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
        },
    };

    return (
        <div className='mt-5'>
            <h1>Brand Budget Statistics</h1>
            <Bar data={chartData} options={options} />
        </div>
    );
};

export default BudgetStatistics;
