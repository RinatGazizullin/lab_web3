const errorElement = document.getElementById("global-error");
const input = document.getElementById("mainForm:Yinput");

input.addEventListener('input', function(event) {
    const y = parseFloat(event.target.value.trim().replace(/,/g, '.'));
    if (isNaN(y) || y < -5 || y > 3) {
        showError("Y должен быть в от -5 до 3");
    } else {
        hideError();
    }
});

function showError(msg) {
    if (errorElement.classList.contains("hidden")) {
        errorElement.classList.remove("hidden");
    }
    errorElement.innerText = msg;
}

function hideError() {
    if (!errorElement.classList.contains("hidden")) {
        errorElement.classList.add("hidden");
    }
}
