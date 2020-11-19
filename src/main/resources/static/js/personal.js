const requestGetURL = 'http://localhost:9001/email/getUser';

function sendRequest(method, url, body = null) {
    return fetch(url).then(response => {
        return response.json();
    })
}

var nameInput = document.getElementById("name");
var passwordInput = document.getElementById("password");

sendRequest('GET', requestGetURL).then(data => {
    nameInput.value = data["name"];
    passwordInput.value = data["password"]
}).catch(err => console.log(err))

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

function isEmpty(str) {
	return str.trim() === '';
}

passwordInput.addEventListener('keydown', without_trims);

var changeButton = document.getElementById("changeButton");
var saveButton = document.getElementById('saveButton');

var onChange = false;

changeButton.onclick = function() {
    if (!onChange) {
        nameInput.readOnly = false;
        passwordInput.readOnly = false;

        saveButton.disabled = false;
        onChange = true;
    }
}

document.querySelector("form").addEventListener("submit", submitForm);

function submitForm(event) {
    console.log("submit");
    event.preventDefault();
    console.log("submit");
    let obj = {};
    obj['name'] = nameInput.value;
    obj['password'] = passwordInput.value;

    let request = new Request(event.target.action, {
        method: 'POST',
        body: JSON.stringify(obj),
        headers: {
            'Content-Type': 'application/json',
        },
    });

    fetch(request).then(response => console.log("response")).catch(err => console.log(err));
}