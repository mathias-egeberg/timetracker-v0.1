document.addEventListener('DOMContentLoaded', () => {
    const ctx = document.getElementById('timeEntryChart').getContext('2d');
    let currentStartDate = new Date();
    currentStartDate.setDate(currentStartDate.getDate() - currentStartDate.getDay() + 1); // Set to Monday of the current week

    const fetchData = (startDate) => {
        const formattedDate = startDate.toISOString().split('T')[0];
        return fetch(`http://localhost:8080/time-entries/week?userId=1&startDate=${formattedDate}`)
            .then(response => response.json());
    };

    const renderChart = (data) => {
        const labels = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
        const durations = new Array(7).fill(0);

        data.forEach(entry => {
            const dayOfWeek = new Date(entry.startTime).getDay();
            const index = dayOfWeek === 0 ? 6 : dayOfWeek - 1; // Adjust for Sunday being 0 in getDay()
            durations[index] += entry.duration;
        });

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Hours Worked',
                    data: durations,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    };

    const loadDataAndRenderChart = (startDate) => {
        fetchData(startDate).then(renderChart);
    };

    document.getElementById('prevWeek').addEventListener('click', () => {
        currentStartDate.setDate(currentStartDate.getDate() - 7);
        loadDataAndRenderChart(currentStartDate);
    });

    document.getElementById('nextWeek').addEventListener('click', () => {
        currentStartDate.setDate(currentStartDate.getDate() + 7);
        loadDataAndRenderChart(currentStartDate);
    });

    loadDataAndRenderChart(currentStartDate);
});