const hourHand = document.getElementById('hour-hand');
const minuteHand = document.getElementById('minute-hand');
const secondHand = document.getElementById('second-hand');
const digitalTimeElement = document.getElementById('digital-time');

function setDate() {
    const now = new Date();

    const millis =  now.getMilliseconds();
    const millisDegrees = ((millis / 60000) * 360);

    const seconds = now.getSeconds();
    const secondsDegrees = ((seconds / 60) * 360);
    secondHand.style.transform = `translateX(-50%) rotate(${secondsDegrees + millisDegrees}deg)`;

    const minutes = now.getMinutes();
    const minutesDegrees = ((minutes / 60) * 360) + ((seconds / 60) * 6);
    minuteHand.style.transform = `translateX(-50%) rotate(${minutesDegrees}deg)`;

    const hours = now.getHours();
    const hoursDegrees = ((hours % 12) / 12) * 360 + ((minutes / 60) * 30);
    hourHand.style.transform = `translateX(-50%) rotate(${hoursDegrees}deg)`;

    const hoursStr = String(hours).padStart(2, '0');
    const minutesStr = String(minutes).padStart(2, '0');
    const secondsStr = String(seconds).padStart(2, '0');
    digitalTimeElement.textContent = `${hoursStr}:${minutesStr}:${secondsStr}`;
}

setInterval(setDate, 1);
setDate();
