const url = new URL(window.location.href);
let error = document.querySelector('.error');
if (url.searchParams.get("isCorrect") == 'false') {
    error.style.display = 'block';
} else {
    error.style.display = 'none';
}

var submitForm = document.querySelector('.login-div')
var email = document.querySelector('.email')
var passwordForm = document.querySelector('.password');

const minLength = 8;
const spaceKeyCode = 32;

var without_trims = function(e) {
	if (e.keyCode == spaceKeyCode) {
		e.preventDefault();
	}
}

function checkPassword(password) {
    return !isEmpty(password) && password.length >= minLength && !password.includes(' ');
}

function checkEmail(email) {
    return !isEmpty(email) && email.match(/.+@.+\..+/i) !== null;
}

function isEmpty(str) {
	return str.trim() === '';
}

passwordForm.addEventListener('keydown', without_trims);

submitForm.addEventListener("submit", function(e) {
    if (checkEmail(email.value) && checkPassword(passwordForm.value)) {
        return;
	} else {
	    e.preventDefault();
	    console.log("incorrect value")
	}
}, false);

