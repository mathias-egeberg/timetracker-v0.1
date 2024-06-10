document.addEventListener("DOMContentLoaded", function() {
    const daysContainer = document.querySelector(".days"),
        nextBtn = document.querySelector(".next-btn"),
        prevBtn = document.querySelector(".prev-btn"),
        month = document.querySelector(".month"),
        todayBtn = document.querySelector(".today-btn");

    const months = [
        "Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli",
        "August", "September", "Oktober", "November", "Desember"
    ];

    const date = new Date();
    let currentMonth = date.getMonth();
    let currentYear = date.getFullYear();

    async function fetchCalendarEntries(userId) {
        const response = await fetch(`/home/calendar/calendar-entries?userId=${userId}`);
        if (response.ok) {
            return response.json();
        } else {
            console.error("Failed to fetch calendar entries");
            return [];
        }
    }

    async function renderCalendar() {
        date.setDate(1);
        const firstDay = (new Date(currentYear, currentMonth, 1).getDay() + 6) % 7;
        const lastDayDate = new Date(currentYear, currentMonth + 1, 0).getDate();
        const prevLastDayDate = new Date(currentYear, currentMonth, 0).getDate();
        const nextDays = (7 - new Date(currentYear, currentMonth + 1, 0).getDay() - 1 + 7) % 7;

        month.innerHTML = `${months[currentMonth]} ${currentYear}`;

        let daysHTML = "";

        for (let x = firstDay; x > 0; x--) {
            daysHTML += `<div class="day prev">${prevLastDayDate - x + 1}</div>`;
        }

        const userId = document.querySelector('meta[name="userId"]').content;
        const calendarEntries = await fetchCalendarEntries(userId);

        for (let i = 1; i <= lastDayDate; i++) {
            const currentDate = new Date(currentYear, currentMonth, i);
            const isToday = i === new Date().getDate() &&
                currentMonth === new Date().getMonth() &&
                currentYear === new Date().getFullYear();
            const hasEntry = calendarEntries.some(entry =>
                new Date(entry).toDateString() === currentDate.toDateString()
            );

            daysHTML += `<div class="day ${isToday ? 'today' : ''} ${hasEntry ? 'entry' : ''}">${i}</div>`;
        }

        for (let j = 1; j <= nextDays; j++) {
            daysHTML += `<div class="day next">${j}</div>`;
        }

        daysContainer.innerHTML = daysHTML;
    }

    renderCalendar();

    nextBtn.addEventListener("click", () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        renderCalendar();
    });

    prevBtn.addEventListener("click", () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        renderCalendar();
    });

    todayBtn.addEventListener("click", () => {
        currentMonth = date.getMonth();
        currentYear = date.getFullYear();
        renderCalendar();
    });
});
