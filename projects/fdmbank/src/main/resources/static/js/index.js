const container = document.getElementById('container');
const registerButton = document.getElementById('register');
const loginButton = document.getElementById('login');

registerButton.addEventListener('click', () => {
    container.classList.add("active");
});

loginButton.addEventListener('click', () => {
    container.classList.remove("active");
});


function togglePasswordVisibility(inputId, iconId) {
    var input = document.getElementById(inputId);
    var icon = document.getElementById(iconId);

    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}

function checkPasswordStrength() {
    var password = document.getElementById("password").value;
    var sum = 0;
    var strength = "Weak";
    var color = "red";

    if (password.length < 4) sum -= 1;
    if (password.length < 6) sum -= 1;
    if (password.length >= 8) sum += 2;
    if (password.length >= 10) sum += 2;
    if (password.length >= 12) sum += 2;
    if (password.match(/[a-z]/g)) sum += 1;
    if (password.match(/\d/g)) sum += 1;
    if (password.match(/[A-Z]/g)) sum += 1;
    if (password.match(/[^a-zA-Z\d]/g)) sum += 1;

    if (sum >= 9) {
        strength = "Strong";
        color = "green";
    } else if (sum >= 4) {
        strength = "Medium";
        color = "orange";
    }

    document.getElementById("passwordStrength").innerText = "Password Strength: " + strength;
    document.getElementById("passwordStrength").style.color = color;
    document.getElementById("passwordStrength").style.display = password.length > 0 ? "block" : "none";
}

function hidePasswordStrength() {
    document.getElementById("passwordStrength").style.display = "none";
};
