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

function updateClock(clock, timezone = null) {
    const now = timezone ? getTimeForTimezone(timezone) : new Date();

    const hours = now.getHours() % 12;
    const minutes = now.getMinutes();
    const seconds = now.getSeconds();

    const hourDeg = (hours * 30) + (minutes * 0.5);
    const minuteDeg = (minutes * 6) + (seconds * 0.1);
    const secondDeg = seconds * 6;

    const hourHand = clock.querySelector('.hour-hand');
    const minuteHand = clock.querySelector('.minute-hand');
    const secondHand = clock.querySelector('.second-hand');

    hourHand.style.transform = `translateX(-50%) rotate(${hourDeg}deg)`;
    minuteHand.style.transform = `translateX(-50%) rotate(${minuteDeg}deg)`;
    secondHand.style.transform = `translateX(-50%) rotate(${secondDeg}deg)`;
}

function updateAllClocks() {
    const clocks = document.querySelectorAll('.clock');
    const timezones = [
        'Europe/Moscow',
        'America/New_York',
        null,
        'Asia/Shanghai',
        'Europe/London'
    ];

    clocks.forEach((clock, index) => {
        const tz = timezones[index];
        updateClock(clock, tz);
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

updateAllClocks();
startClock();
