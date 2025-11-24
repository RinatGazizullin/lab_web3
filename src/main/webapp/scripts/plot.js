"use strict";

const canvas = document.getElementById("plot");
const ctx = canvas.getContext("2d");

canvas.width = canvas.offsetWidth * 4;
canvas.height = canvas.offsetHeight * 4;

let centerX = canvas.width / 2;
let centerY = canvas.height / 2;
let scale = Math.min(canvas.width, canvas.height) / 3;

function drawArrow(x1, y1, x2, y2, k1, k2, arrowSize = 60) {
    const angle = Math.atan2(y2 - y1, x2 - x1);

    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2 + k1, y2 + k2);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(x2, y2);
    ctx.lineTo(
        x2 - arrowSize * Math.cos(angle - Math.PI / 6),
        y2 - arrowSize * Math.sin(angle - Math.PI / 6)
    );
    ctx.lineTo(
        x2 - arrowSize * Math.cos(angle + Math.PI / 6),
        y2 - arrowSize * Math.sin(angle + Math.PI / 6)
    );
    ctx.closePath();
    ctx.fillStyle = 'rgb(190, 195, 199)';
    ctx.fill();
}

function drawPlot() {
    ctx.beginPath();
    ctx.moveTo(61, centerY);
    ctx.lineTo(canvas.width - 61, centerY);
    ctx.strokeStyle = 'rgb(190, 195, 199)';
    ctx.lineWidth = 8;
    ctx.stroke();
    drawArrow(
        canvas.width - 61, centerY,
        canvas.width - 30, centerY,
        -11, 0
    );

    ctx.beginPath();
    ctx.moveTo(centerX, 61);
    ctx.lineTo(centerX, canvas.height - 61);
    ctx.strokeStyle = 'rgb(190, 195, 199)';
    ctx.lineWidth = 8;
    ctx.stroke();
    drawArrow(
        centerX, 61,
        centerX, 31,
        0, 11
    );

    ctx.beginPath();
    ctx.lineTo(centerX - scale * .5, centerY);
    ctx.lineTo(centerX, centerY - scale * .5);
    ctx.arc(
        centerX,
        centerY,
        scale * .5,
        Math.PI * 3 / 2,
        0
    );
    ctx.lineTo(centerX + scale * .5, centerY + scale);
    ctx.lineTo(centerX, centerY + scale);
    ctx.lineTo(centerX, centerY);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();

    ctx.fillStyle = 'rgba(52, 152, 219, 0.6)';
    ctx.fill()

    ctx.strokeStyle = '#3498db';
    ctx.lineWidth = 4;
    ctx.stroke();

    ctx.font = '4rem Arial bold';
    ctx.fillStyle = 'black';

    ctx.fillText('x', canvas.width - 121, centerY - 30);
    ctx.fillText('y', centerX + 31, 121);

    ctx.fillText('-R', centerX - scale - 10, centerY + 61);
    ctx.fillText('-R/2', centerX - scale * 0.5 - 15, centerY + 60);
    ctx.fillText('R/2', centerX + scale * 0.5 - 5, centerY + 60);
    ctx.fillText('R', centerX + scale - 5, centerY + 60);

    ctx.fillText('-R', centerX + 10, centerY + scale + 10);
    ctx.fillText('-R/2', centerX + 10, centerY + scale * 0.5 + 10);
    ctx.fillText('R/2', centerX + 10, centerY - scale * 0.5 - 5);
    ctx.fillText('R', centerX + 10, centerY - scale - 5);
}

function drawPoint(x, y, r, hit) {
    const px = centerX + (x * scale) / r;
    const py = centerY - (y * scale) / r;
    ctx.beginPath();
    ctx.arc(px, py, 20, 0, 2 * Math.PI);
    ctx.fillStyle = hit ? 'green' : 'red';
    ctx.fill();
    ctx.strokeStyle = hit ? 'darkgreen' : 'darkred';
    ctx.lineWidth = 4;
    ctx.stroke();
    ctx.closePath();
}

function clearPlot() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPlot();
}

function drawPoints(data, r) {
    const points = JSON.parse(data);
    clearPlot();
    if (Array.isArray(points)) {
        points.forEach((point) => {
            if (isValid(point) && r.includes(point.request.r)) {
                drawPoint(point.request.x, point.request.y, point.request.r, point.result === 'HIT');
            }
        })
    }
}

function isValid(point) {
    if (!point || !point.request || !point.result) {
        return false;
    }
    const x = point.request.x;
    const y = point.request.y;
    const r = point.request.r;
    return x !== null && y !== null && r !== null &&
        typeof x === 'number' &&
        typeof y === 'number' &&
        typeof r === 'number' &&
        typeof point.result === 'string';
}

/*
canvas.addEventListener('click', function(event) {
    console.log(123);
    const rect = canvas.getBoundingClientRect();
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;

    const r = parseFloat(document.getElementById('clickR').value);
    const x = (event.clientX - rect.left) * scaleX;
    const y = (event.clientY - rect.top) * scaleY;

    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    const graphX = (x - centerX) * r / scale;
    const graphY = (centerY - y) * r / scale;

    console.log("Клик на графике:", graphX, graphY, r, x, y);

    document.getElementById('clickX').value = graphX.toFixed(6);
    document.getElementById('clickY').value = graphY.toFixed(6);

    document.getElementById('mainForm:j_idt20').value = graphX.toFixed(6); // Y
    document.querySelector('#mainForm\\:x').value = graphX.toFixed(6); // X (если это selectOneMenu)

    document.getElementById('mainForm:submitClick').click();
});
 */

canvas.addEventListener('click', function(event) {
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left);
    const y = (event.clientY - rect.top);

    const oldR = document.getElementById("RString").value;
    const graphX = 4 * (x - centerX / 4) / scale;
    const graphY = 4 * (centerY / 4 - y) / scale;
    for (let RString of oldR.split(",")) {
        const R = parseFloat(RString);
        document.getElementById("customForm:customX").value = (graphX * R).toFixed(6);
        document.getElementById("customForm:customY").value = (graphY * R).toFixed(6);
        document.getElementById("customForm:customR").value = RString;
        document.getElementById('customForm:custom').click();
    }

    document.getElementById("customForm:customR").value = oldR;
    document.getElementById('customForm:update').click();
});

addEventListener("resize", (event) => {
    location.reload();
})
