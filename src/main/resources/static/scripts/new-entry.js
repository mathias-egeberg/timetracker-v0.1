function calculateDuration() {
    // Get the start time and end time input elements
    var startTimeInput = document.getElementById("start_time");
    var endTimeInput = document.getElementById("end_time");

    // Get the output duration input element
    var durationInput = document.getElementById("duration");

    // Parse the selected start and end times
    var startTime = new Date(startTimeInput.value);
    var endTime = new Date(endTimeInput.value);

    // Calculate the difference in milliseconds
    var durationMilliseconds = Math.abs(endTime - startTime);

    // Convert milliseconds to minutes
    var durationMinutes = Math.floor(durationMilliseconds / (1000 * 60));

    // Set the calculated duration in the duration input field
    durationInput.value = (durationMinutes/60).toFixed(1);
}



