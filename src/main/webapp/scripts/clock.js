function getTimeForTimezone(timezone) {
    const now = new Date();
    const options = {
        timeZone: timezone,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    };

    const formatter = new Intl.DateTimeFormat('en-CA', options);
    const parts = formatter.formatToParts(now);

    const hour = parseInt(parts.find(p => p.type === 'hour').value);
    const minute = parseInt(parts.find(p => p.type === 'minute').value);
    const second = parseInt(parts.find(p => p.type === 'second').value);

    const time = new Date();
    time.setHours(hour, minute, second, 0);
    return time;
}

function updateClock(clock, timezone = null, label) {
    const now = timezone ? getTimeForTimezone(timezone) : new Date();

    const hours = now.getHours() % 12;
    const minutes = now.getMinutes();
    const seconds = now.getSeconds();

    drawClock(clock, hours >= 20 || hours <= 6, hours, minutes, seconds, label);
}

function updateAllClocks() {
    const clocks = document.querySelectorAll('.clock');
    const timezones = [
        'Europe/Moscow',
        'America/New_York',
        null,
        'Europe/London'
    ];
    const labels = [
        'MSC',
        'NYC',
        'CURRENT',
        'LON'
    ]

    clocks.forEach((clock, index) => {
        updateClock(clock, timezones[index], labels[index]);
    });
}

function startClock() {
    const now = new Date();
    const delay = 1000 - now.getMilliseconds();

    setTimeout(function() {
        updateAllClocks();
        setInterval(updateAllClocks, 1000);
    }, delay);
}

function drawClock(canvas, isNight, hours, minutes, seconds, label) {
    canvas.width = canvas.offsetWidth * 4;
    canvas.height = canvas.offsetHeight * 4;

    const backColor = isNight ? "rgb(61, 61, 61)" : "white"; // или rgb(31,31,31)

    const hoursMarks = isNight ? "rgb(181, 181, 181)" : "rgb(151, 151, 151)";
    const minMarks = isNight ? "rgb(121, 121, 121)" : "rgb(201, 201, 201)";

    // const hourHand = isNight ? "white" : "rgb(61, 61, 61)";
    // const minHand = isNight ? "white"  : "rgb(61, 61, 61)";
    const hand = isNight ? "white"  : "rgb(61, 61, 61)";

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const ctx = canvas.getContext("2d");
    const radius = Math.min(centerX, centerY);

    ctx.lineCap = "round";
    ctx.fillStyle = backColor;
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    for (let time = 0; time < 60; time++) {
        const angle = (time * 6) * Math.PI / 180;

        let startRadius;
        let endRadius;
        if (time % 5 === 0) {
            ctx.strokeStyle = hoursMarks;
            ctx.lineWidth = 10;
            startRadius = radius * 0.88;
            endRadius = radius * 0.95;
        } else {
            ctx.strokeStyle = minMarks;
            ctx.lineWidth = 10;
            startRadius = radius * 0.88;
            endRadius = radius * 0.92;
        }

        const startX = centerX + startRadius * Math.sin(angle);
        const startY = centerY - startRadius * Math.cos(angle);
        const endX = centerX + endRadius * Math.sin(angle);
        const endY = centerY - endRadius * Math.cos(angle);

        ctx.beginPath();
        ctx.moveTo(startX, startY);
        ctx.lineTo(endX, endY);
        ctx.stroke();
    }

    ctx.font = '5rem Arial bold';

    ctx.fillStyle = '#8f8f8f';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(label, centerX, centerY - radius * .55);

    let secRadians = -(Math.PI * 2 * seconds) / 60;
    let minRadians = -(Math.PI * 2 * minutes) / 60 + secRadians / 60;
    let hoursRadians = -(Math.PI * 2 * (hours % 12)) / 12 + minRadians / 12;

    ctx.beginPath();
    ctx.strokeStyle = hand;
    ctx.lineWidth = 20;
    ctx.moveTo(centerX - 0.1 * radius * Math.sin(-hoursRadians), centerY + 0.1 * radius * Math.cos(-hoursRadians));
    ctx.lineTo(centerX - 0.6 * radius * Math.sin(hoursRadians), centerY - 0.6 * radius * Math.cos(hoursRadians));
    ctx.stroke();

    ctx.beginPath();
    ctx.strokeStyle = hand;
    ctx.lineWidth = 16;
    ctx.moveTo(centerX - 0.1 * radius * Math.sin(-minRadians), centerY + 0.1 * radius * Math.cos(-minRadians));
    ctx.lineTo(centerX - 0.8 * radius * Math.sin(minRadians), centerY - 0.8 * radius * Math.cos(minRadians));
    ctx.stroke();

    ctx.beginPath();
    ctx.fillStyle = hand;
    ctx.arc(centerX, centerY, radius * 0.04, 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();

    ctx.beginPath();
    ctx.strokeStyle = "orange"
    ctx.lineWidth = 8;
    ctx.moveTo(centerX - 0.1 * radius * Math.sin(-secRadians), centerY + 0.1 * radius * Math.cos(-secRadians));
    ctx.lineTo(centerX - 0.92 * radius * Math.sin(secRadians), centerY - 0.92 *  radius * Math.cos(secRadians));
    ctx.stroke();

    ctx.beginPath();
    ctx.fillStyle = backColor;
    ctx.arc(centerX, centerY, radius * 0.03, 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();
}

function start() {
    updateAllClocks();
    startClock();
}

window.addEventListener('load', start);
