var submitForm = document.querySelector('.form')
var email = document.querySelector('.login')
var passwordForm = document.querySelector('.password');

const minLength = 8;
const spaceKeyCode = 32;

var without_trims = function(e) {
	if (e.keyCode == spaceKeyCode) {
		e.preventDefault();
	}
}

function checkPassword(password) {
    return !isEmpty(password) && password.length >= minLength;
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

