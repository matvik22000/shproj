let teachers = getTeachers();
let rooms;
let roomsnotnumb = [];
let roomssortbyabc;
let roomtypes;
let me;
let isinput = false;
let tablenow = 0;
let x = 50;
let value = '';
let firsttime = false;
let reservations = getReservations(0, 10000000000000);
let lasttbl2x;
let focus = false;


getRooms();
getRoomTypes();
ChooseTableType();
CreateDatePick1();
CreateSearch();
CreateTables();
Header(true);
init();
ChooseMenuItem();
switchView(1);
CreateDatePick();
MakeSortMenu();
MakContForRoomsByName(document.getElementById('cont_for_rooms_names'));
ButtonStyle(document.getElementById('button'), 0);
ButtonStyle(document.getElementById('button1'), 3);
OnFocus();

function CreateTables() {
    CreateTableFirstType();
    CreateTableSecondType();
    CreateTableThirdType();
}

function CreateSearch() {
    let search_box = document.getElementById('search_box');
    search_box.oninput = function () {
        value = search_box.value;
        CreateTables();
    };
    let login = document.getElementById('log');
    let pass = document.getElementById('pass');
    login.oninput = function () {
        login.style.background = 'rgba(23, 162, 184, 0.12)';
        if (login.value === '') {
            login.style.background = 'rgb(255,202,188)';
        }
    };
    pass.oninput = function () {
        pass.style.background = 'rgba(23, 162, 184, 0.12)';
        if (pass.value === '') {
            pass.style.background = 'rgb(255,202,188)';
        }
    };
    let date = document.getElementById('date');
    let date1 = document.getElementById('date1');
    let startt = document.getElementById('start_time');
    let endt = document.getElementById('end_time');
    let lasts = 0;
    let lastd = 0;
    let laste = 0;
    let lastd1 = 0;
    date.oninput = function () {
        if ((date.value.length === 2 || date.value.length === 5) && lastd - date.value.length <= 0) {
            date.value += '.';
        }
        if (date.value.length === 10) {
            startt.focus()
        }
        lastd = date.value.length;
    };
    date1.oninput = function () {
        if ((date1.value.length === 2 || date1.value.length === 5) && lastd1 - date1.value.length <= 0) {
            date1.value += '.';
        }
        if (date1.value.length === 10) {
            reservations = getReservations(StringToDate(date1.value, '00:00').getTime(), 86400000 + StringToDate(date1.value, '00:00').getTime());
            CreateTables();
        }
        lastd1 = date1.value.length;
    };
    startt.oninput = function () {
        if (startt.value.length === 2 && lasts - startt.value.length <= 0) {
            startt.value += ':';
        }
        if (startt.value.length === 5) {
            endt.focus()
        }
        lasts = startt.value.length;
    };
    endt.oninput = function () {
        if (endt.value.length === 2 && laste - endt.value.length <= 0) {
            endt.value += ':';
        }
        laste = endt.value.length;
    }
}

function init() {
    if (document.layers) document.captureEvents(Event.MOUSEMOVE);
    document.onmousemove = mousemove;
}

function CreateDatePick1() {
    let width = 0;
    let searchdate = document.getElementById('search_date');
    let date = document.getElementById('date1');
    date.style.fontSize = '20px';
    date.style.paddingLeft = '15px';
    date.style.paddingRight = '5px';
    let calendar = document.getElementById('calendar1');
    calendar.style.fontSize = '20px';
    calendar.style.color = 'rgb(23,162,184)';
}

function CreateDatePick() {
    let width = 0;
    let searchdate = document.getElementById('search_date');
    let date = document.getElementById('date');
    date.style.fontSize = '20px';
    date.style.paddingLeft = '15px';
    date.style.paddingRight = '5px';
    let calendar = document.getElementById('calendar');
    calendar.style.fontSize = '20px';
    calendar.style.color = 'rgb(23,162,184)';
}

function OnFocus() {
    let room_name = document.getElementById('room_name');
    let ras = document.getElementsByClassName('ras')[0];
    room_name.onfocus = function () {
        ras.style.height = '200px';
        ras.style.transition = '0.6s';
    };
    room_name.onblur = function () {
        setTimeout(function () {
            if (!focus) {
                ras.style.height = '0';
                ras.style.transition = '0.6s';
            }
        }, 500);

    }
}

function Create_Week_Picker() {
    let weekpickcomt = document.getElementById('week_pick_cont');
    let x = weekpickcomt.clientWidth / 7;
    console.log(x);
    let booleanarr = [];
    for (let i = 0; i < 7; i++) {
        booleanarr[i] = false;
    }
    let weekdays = ['пн', 'вт', 'ср', 'чт', 'пт', 'сб', 'вс'];
    let arr = [];
    for (let i = 0; i < 7; i++) {
        let canvas = document.createElement('canvas');
        canvas.style.width = `30px`;
        canvas.style.height = '30px';
        canvas.style.zIndex = '0';
        canvas.width = canvas.height;
        canvas.className = 'dayweekpick';
        canvas.style.transform = `translate(${i * x + (x - 30) / 2}px, 0)`;
        weekpickcomt.appendChild(canvas);
        arr[i] = canvas;
    }
    for (let i = 0; i < 7; i++) {
        let dv = document.createElement('div');
        dv.className = 'dayweekpick';
        dv.style.width = `${x}px`;
        dv.style.height = '30px';
        dv.style.textAlign = 'center';
        dv.style.fontSize = '20px';
        dv.style.paddingTop = '2px';
        dv.appendChild(document.createTextNode(weekdays[i]));
        dv.style.transform = `translate(${i * x}px, 0)`;
        weekpickcomt.appendChild(dv);
        dv.onmouseover = function () {
            if (!booleanarr[i]) {
                let start = Date.now(); // запомнить время начала
                let timer = setInterval(function () {
                    // сколько времени прошло с начала анимации?
                    let timePassed = Date.now() - start;

                    if (timePassed >= 300) {
                        clearInterval(timer); // закончить анимацию через 2 секунды
                        return;
                    }

                    let ctx = arr[i].getContext("2d");
                    ctx.fillStyle = "rgb(206,206,206)";
                    ctx.beginPath();
                    ctx.arc(75, 75, timePassed / 300 * 75, 0, Math.PI * 2, false);
                    ctx.fill();
                }, 20);
            }
        };
        dv.onmouseout = function () {
            if (!booleanarr[i]) {
                let start = Date.now(); // запомнить время начала
                let timer = setInterval(function () {
                    // сколько времени прошло с начала анимации?
                    let timePassed = Date.now() - start;

                    if (timePassed >= 300) {
                        clearInterval(timer); // закончить анимацию через 2 секунды
                        return;
                    }

                    let ctx = arr[i].getContext("2d");
                    ctx.fillStyle = "rgb(206,206,206)";
                    ctx.clearRect(0, 0, 150, 150);
                    ctx.beginPath();
                    ctx.arc(75, 75, 75 - timePassed / 300 * 75, 0, Math.PI * 2, false);
                    ctx.fill();
                    ctx.closePath();
                }, 20);
            }
        };
        dv.onmousedown = function () {
            booleanarr[i] = !booleanarr[i];
            let ctx = arr[i].getContext("2d");
            ctx.fillStyle = "rgb(170,170,170)";
            ctx.clearRect(0, 0, 150, 150);
            ctx.beginPath();
            ctx.arc(75, 75, 75, 0, Math.PI * 2, false);
            ctx.fill();
            ctx.closePath();
        };
        dv.onmouseup = function () {
            let ctx = arr[i].getContext("2d");
            ctx.fillStyle = "rgb(206,206,206)";
            ctx.clearRect(0, 0, 150, 150);
            ctx.beginPath();
            ctx.arc(75, 75, 75, 0, Math.PI * 2, false);
            ctx.fill();
            ctx.closePath();
        };
    }
}

function getRooms() {
    let con = new XMLHttpRequest();
    con.open('GET', `http://shproj2020.herokuapp.com/get_rooms_list`, false);
    con.withCredentials = true;
    con.send();
    rooms = [];
    let response1 = JSON.parse(con.response);
    console.log("rooms: " + response1.length);
    let j = 1000;
    for (let i = 0; i < response1.length; i++) {
        if (Number.isInteger(+response1[i].classNumber)) {
            rooms[response1[i].classNumber] = response1[i];
        } else {
            rooms[j] = response1[i];
            j++;
        }
    }
}

function getRoomTypes() {
    let con = new XMLHttpRequest();
    con.open('GET', `http://shproj2020.herokuapp.com/get_room_types_list`, false);
    con.withCredentials = true;
    con.send();
    roomtypes = [];
    let response1 = JSON.parse(con.response);
    console.log("roomtypes: " + response1.length);
    for (let i = 0; i < response1.length; i++) {
        roomtypes[response1[i].typeId] = response1[i];
    }
}

function MakContForRoomsByType() {
    let types = document.getElementById('cont_for_rooms_types');
    let for_types = [];
    let gg = [];
    for (let i = 0; i < rooms.length; i++) {
        if (rooms[i]) {
            let arr = rooms[i].classTypes;
            if (arr) {
                for (let j = 0; j < arr.length; j++) {
                    if (!for_types[j]) {
                        for_types[j] = [];
                    }
                    for_types[j][for_types[j].length] = i;
                }
            } else {
                gg[gg.length] = i;
            }
        }
    }
    let div = document.createElement('div');
    div.className = 'for_div';
    let div0 = document.createElement('div');
    for (let i = 0; i < for_types.length; i++) {

    }

}

function MakContForRoomsByName(names) {
    let search_box = document.getElementById('room_name');
    let div = document.createElement('div');
    div.className = 'for_div';
    let children = 0;
    let last = -1;
    let o = [];
    for (let i = 0; i < rooms.length; i++) {
        let div0 = document.createElement('div');
        div0.className = 'for_div0';
        o[i] = div0;
        if (rooms[i]) {
            div0.appendChild(document.createTextNode(rooms[i].classNumber));
            let bool = false;
            div0.onmouseover = function () {
                div0.style.background = '#c0c0c0';
                div0.style.transition = '0.3s';
            };
            let y = false;
            div0.onmouseout = function () {
                if (!bool) {
                    div0.style.background = 'none';
                    div0.style.transition = '0.3s';
                    y = false;
                }

            };
            div0.onmouseup = function () {
                if (y) {
                    bool = !bool;
                    if (!bool) {
                        search_box.value = '';
                    } else {
                        search_box.value = rooms[i].classNumber;
                    }
                    div0.style.background = '#c0c0c0';
                    div0.style.transition = '0.3s';
                    for (let j = 0; j < o.length; j++) {
                        if (o[j] && j !== i) {
                            o[j].style.background = 'none';
                            o[j].style.transition = '0.3s';
                        }
                    }
                }
            };
            div0.onmousedown = function () {
                div0.style.background = '#747474';
                div0.style.transition = '0s';
                y = true;
            };
            if (i < 1000) {
                if (last === -1) {
                    last = i;
                    div.appendChild(div0);
                    children++;
                } else {
                    if (+i.toString()[0] - last.toString()[0] === 0 && i.toString().length === last.toString().length) {
                        div.appendChild(div0);
                        children++;
                    } else {
                        names.appendChild(div);
                        children = 1;
                        div = document.createElement('div');
                        div.className = 'for_div';
                        div.appendChild(div0);
                    }
                }
                last = i;
                if (i + 1 === rooms.length) {
                    names.appendChild(div);
                }
            } else {
                if (children > 0) {
                    names.appendChild(div);
                    children = 0;
                    div = document.createElement('div');
                    div.className = 'for_div';
                }
                div.appendChild(div0);
                if (i + 1 === rooms.length) {
                    names.appendChild(div);
                }
            }
        }
    }
}

function MakeSortMenu() {
    let g1 = document.getElementById('sort_by_name');
    g1.style.color = 'rgb(255,255,255)';
    let cont = document.getElementById("cont_for_rooms_containers_type");
    let g2 = document.getElementById('sort_by_type');
    let block = document.getElementById("block_for_sort");
    let array = [g1, g2];
    let b = true;
    let last = 0;
    let i0 = 0;
    let bool = [true, false, false, false];
    for (let i = 0; i < array.length; i++) {
        array[i].style.transform = `translate(${i * 100}%, 0)`;
        let j = array[i].getBoundingClientRect().x - block.getBoundingClientRect().x;
        array[i].onmouseover = function () {
            block.style.transform = `translate(${j}px, 0)`;
            block.style.background = "rgb(20,140,159)";
            array[i].style.color = 'rgb(255,255,255)';
            array[i].style.transition = '0.6s';
            block.style.transition = '0.6s';
            for (let k = 0; k < array.length; k++) {
                if (k !== i) {
                    array[k].style.color = 'rgb(0,0,0)';
                    array[k].style.transition = '0.6s';
                    bool[k] = false;
                }
            }
        };
        array[i].onmouseout = function () {
            if (!bool[i]) {
                block.style.transform = `translate(${last}px, 0)`;
                block.style.background = "rgb(23,162,184)";
                array[i].style.color = 'rgb(0,0,0)';
                array[i].style.transition = '0.6s';
                block.style.transition = '0.6s';
                for (let k = 0; k < array.length; k++) {
                    bool[i0] = true;
                    if (bool[k]) {
                        array[k].style.color = 'rgb(255,255,255)';
                        array[k].style.transition = '0.6s';
                    }
                }
            }
        };
        array[i].onmousedown = function () {
            last = j;
            i0 = i;
            bool[i] = !bool[i];
        }
    }
}

function mousemove(event) {
    let mouse_x = mouse_y = 0;
    if (document.attachEvent != null) {
        mouse_x = window.event.clientX;
        mouse_y = window.event.clientY;
    } else if (!document.attachEvent && document.addEventListener) {
        mouse_x = event.clientX;
        mouse_y = event.clientY;
    }
    let menu = document.getElementById('menu');
    if (mouse_x < x) {
        menu.style.transform = 'translate(0, 0)';
        menu.style.boxShadow = '0 0 10px rgba(0, 0, 0, .5)';
        menu.style.transition = '0.6s';
        x = 300;
    } else {
        menu.style.transform = 'translate(-250px, 0)';
        menu.style.boxShadow = '0 0 0 rgba(0, 0, 0, .5)';
        menu.style.transition = '0.6s';
        x = 50;
    }
}

function ChooseMenuItem() {
    let array = document.getElementsByClassName('item');
    for (let i = 0; i < array.length; i++) {
        let bool = false;
        let g = false;
        let y = array[i];
        array[i].onmouseover = function () {
            y.style.background = "rgba(23,162,184,0.34)";
            y.style.transition = '0.6s';
            y.style.paddingLeft = '50px';

        };
        array[i].onmouseout = function () {
            if (!bool) {
                y.style.background = "rgb(174,210,222)";
                y.style.transition = '0.6s';
                y.style.paddingLeft = '20px';
                g = false;
            }
        };
        array[i].onmousedown = function () {
            y.style.background = "rgba(23,162,184,0.67)";
            y.style.transition = '0.2s background';
            y.style.paddingLeft = '50px';
            g = true;
        };
        array[i].onmouseup = function () {
            if (g) {
                bool = true;
                y.style.background = "rgba(23,162,184,0.34)";
                switchView(i + 1);
                for (let j = 0; j < array.length; j++) {
                    if (j !== i) {
                        array[j].style.background = "rgb(174,210,222)";
                        array[j].style.transition = '0.6s';
                        array[j].style.paddingLeft = '20px';
                    }
                }
                g = false;
            }
        };
    }
}

function Header(bool) {
    if (bool) {
        let header = document.getElementById('header');
        if (header.children) {
            ToKillChildren(header);
        }
        let button = document.createElement('input');
        button.type = 'button';
        button.value = 'войти';
        button.style.height = '40px';
        button.style.width = '100px';
        button.className = 'button_for_login_in_the_header';
        let x = (header.clientWidth - 100) / 2;
        let y = (header.clientHeight - 40) / 2;
        button.style.background = "rgb(23,162,184)";
        button.style.transform = `translate(${x}px, ${y}px)`;
        ButtonStyle(button, 1);
        button.appendChild(document.createTextNode('Войти'));
        header.appendChild(button);
    } else {
        let header = document.getElementById('header');
        if (header.children[0].className === 'button_for_login_in_the_header') {
            ToKillChildren(header);
            let name = document.createElement('div');
            let button = document.createElement("div");
            button.className = 'button_for_exit_in_the_header';
            button.style.margin = '10px';
            name.appendChild(document.createTextNode(me.fio));
            ButtonStyle(button, 2);
            name.style.fontSize = '25px';
            name.style.textAlign = 'center';
            button.appendChild(document.createTextNode('Выход'));
            header.appendChild(button);
            header.appendChild(name);
        }

    }
}

function ChooseTableType() {
    let g1 = document.getElementById('g1');
    g1.style.color = 'white';
    let g2 = document.getElementById('g2');
    let g3 = document.getElementById('g3');
    let block = document.getElementById("blockid");
    block.style.background = "rgb(23,162,184)";
    let array = [g1, g2, g3];
    let last = 0;
    let i0 = 0;
    let bool = [true, false, false, false];
    for (let i = 0; i < array.length; i++) {
        array[i].style.transform = `translate(${i * 100}%, 0)`;
        let j = array[i].getBoundingClientRect().x - block.getBoundingClientRect().x;
        array[i].onmouseover = function () {
            block.style.transform = `translate(${j}px, 0)`;
            block.style.background = "rgb(20,140,159)";
            array[i].style.color = 'rgb(255,255,255)';
            array[i].style.transition = '0.6s';
            block.style.transition = '0.6s';
            for (let k = 0; k < array.length; k++) {
                if (k !== i) {
                    array[k].style.color = 'rgb(0,0,0)';
                    array[k].style.transition = '0.6s';
                    bool[k] = false;
                }
            }
        };
        array[i].onmouseout = function () {
            if (!bool[i]) {
                block.style.transform = `translate(${last}px, 0)`;
                block.style.background = "rgb(23,162,184)";
                array[i].style.color = 'rgb(0,0,0)';
                array[i].style.transition = '0.6s';
                block.style.transition = '0.6s';
                for (let k = 0; k < array.length; k++) {
                    bool[i0] = true;
                    if (bool[k]) {
                        array[k].style.color = 'rgb(255,255,255)';
                        array[k].style.transition = '0.6s';
                    }
                }
            }
        };
        let t = false;
        array[i].onmouseup = function () {
            last = j;
            i0 = i;
            bool[i] = !bool[i];
            if (t) {
                tablenow = i;
                switchTable(i);

            }
        };
        array[i].onmousedown = function () {
            t = true;
        }
    }
}

function RequestVerification(con) {
    let answer = con.status;
    if (answer === 200 || answer === 201) {
        return 0;
    } else {
        return 1;
    }
}

function login() {
    let login = document.getElementById("log");
    let pass = document.getElementById("pass");
    let con = new XMLHttpRequest();
    if (login.value && pass.value) {
        con.open('GET', `http://shproj2020.herokuapp.com/login?username=${login.value}&password=${sha256(pass.value)}`, false);
        con.withCredentials = true;
        con.send();
        if (RequestVerification(con) === 1) {
            login.style.background = 'rgb(255,202,188)';
            pass.style.background = 'rgb(255,202,188)';
            pass.value = '';
        } else {
            con = new XMLHttpRequest();
            con.open('GET', `http://shproj2020.herokuapp.com/get_prs_id`, false);
            con.withCredentials = true;
            con.send();
            me = teachers[con.response];
            console.log('id: ' + me.teacherId);
            isinput = true;
            switchView(1);
            Header(false);
            CreateTableFourthType();

        }
    } else {
        if (!pass.value) {
            pass.style.background = 'rgb(255,202,188)';
        }
        if (!login.value) {
            login.style.background = 'rgb(255,202,188)';
        }
    }
}

function getReservations(starttime, endtime) {
    let con = new XMLHttpRequest();
    con.open('GET', `http://shproj2020.herokuapp.com/schedule?startTime=${starttime}&endTime=${endtime}`, false);
    con.send();
    let response = JSON.parse(con.response);
    return response;
}

function getTeachers() {
    let con = new XMLHttpRequest();
    con.open('GET', `http://shproj2020.herokuapp.com/get_teacher_list`, false);
    con.send();
    let response1 = JSON.parse(con.response);
    let teachers = [];
    console.log("teachers: " + response1.length);
    for (let i = 0; i < response1.length; i++) {
        teachers[response1[i].prsId] = response1[i];
    }
    return teachers;
}

function CreateTableFourthType() {
    let tbl = document.getElementById('table4');
    ToKillChildren(tbl);
    for (let i = 0; i < reservations.length; i++) {
        if (me.prsId === reservations[i].teacherId) {
            let div_for_tbl = document.createElement('div');
            let start = new Date(reservations[i].startTime);
            let end = new Date(reservations[i].endTime);
            let div = document.createElement('div');
            div.className = 'divtbl';
            let time = document.createElement('input');
            time.className = 'divtblatr';
            let h = start.toLocaleString("ru", {
                hour: 'numeric',
                minute: 'numeric'
            }) + " - " + end.toLocaleString("ru", {
                hour: 'numeric',
                minute: 'numeric'
            });
            time.value = h;
            let classn = document.createElement('input');
            classn.value = reservations[i].classNumber;
            classn.className = 'divtblatr';
            classn.readOnly = true;
            classn.style.textAlign = 'center';
            let reason = document.createElement('input');
            reason.value = reservations[i].reason;
            reason.className = 'divtblatr';
            reason.readOnly = false;
            time.style.width = '20%';
            time.style.textAlign = 'center';
            classn.style.width = '20%';
            reason.style.width = '50%';
            let delbutt = document.createElement('label');
            let icondel = document.createElement('span');
            icondel.className = 'glyphicon glyphicon-trash';
            delbutt.appendChild(icondel);
            let recbutt = document.createElement('label');
            let iconrec = document.createElement('span');
            recbutt.style.marginRight = '10px';
            iconrec.className = 'glyphicon glyphicon-pencil';
            recbutt.appendChild(iconrec);
            div.appendChild(time);
            div.appendChild(classn);
            div.appendChild(reason);
            div.appendChild(recbutt);
            div.appendChild(delbutt);
            div_for_tbl.appendChild(div);
            tbl.appendChild(div_for_tbl);

        }
    }
}

function CreateTableThirdType() {
    let tbl = document.getElementById('tbl3');
    ToKillChildren(tbl);
    tbl.style.transition = '0s';
    switch (tablenow) {
        case 0:
            tbl.style.transform = `translate(${2 * document.getElementById('tableid').clientWidth}px, -${document.getElementById('tbl1').clientHeight + document.getElementById('tbl2').clientHeight}px)`;
            break;
        case 1:
            tbl.style.transform = `translate(${document.getElementById('tableid').clientWidth}px, -${document.getElementById('tbl1' + document.getElementById('tbl2').clientHeight).clientHeight}px)`;
            break;
        case 2:
            tbl.style.transform = `translate(0, -${document.getElementById('tbl1').clientHeight + document.getElementById('tbl2').clientHeight}px)`;
            break;
    }
    let sortreservations = reservations.sort(function (a, b) {
        if (a.startTime < b.startTime) {
            return -1;
        }
        if (a.startTime > b.startTime) {
            return 1;
        }
        return 0;
    });
    console.log(sortreservations);
    for (let i = 0; i < sortreservations.length; i++) {
        if (sortreservations[i]) {
            let div_for_tbl = document.createElement('div');
            let start = new Date(sortreservations[i].startTime);
            let end = new Date(sortreservations[i].endTime);
            let div = document.createElement('div');
            div.className = 'divtbl';
            let time = document.createElement('div');
            time.className = 'divtblatr';
            let h = start.toLocaleString("ru", {
                hour: 'numeric',
                minute: 'numeric'
            }) + " - " + end.toLocaleString("ru", {
                hour: 'numeric',
                minute: 'numeric'
            });
            time.appendChild(document.createTextNode(h));
            if (h.indexOf(value) !== -1) {
                let name = document.createElement('div');
                name.appendChild(document.createTextNode(teachers[sortreservations[i].teacherId].fio));
                name.className = 'divtblatr';
                name.style.textAlign = 'center';
                let classn = document.createElement('div');
                classn.appendChild(document.createTextNode(sortreservations[i].classNumber));
                classn.className = 'divtblatr';
                classn.style.textAlign = 'center';
                let reason = document.createElement('div');
                reason.appendChild(document.createTextNode(sortreservations[i].reason));
                reason.className = 'divtblatr';
                time.style.width = '1000px';
                time.style.textAlign = 'center';
                name.style.width = '30%';
                classn.style.width = '20%';
                reason.style.width = '30%';
                div.appendChild(time);
                div.appendChild(classn);
                div.appendChild(name);
                div.appendChild(reason);
                div_for_tbl.appendChild(div);
                tbl.appendChild(div_for_tbl);
            }
        }
    }
}

function CreateTableSecondType() {
    let tbl = document.getElementById('tbl2');
    ToKillChildren(tbl);
    console.log(document.getElementById('tbl1').clientHeight);
    tbl.style.transition = '0s';
    switch (tablenow) {
        case 0:
            tbl.style.transform = `translate(${document.getElementById('tableid').clientWidth}px, -${document.getElementById('tbl1').clientHeight}px)`;
            break;
        case 1:
            tbl.style.transform = `translate(0, -${document.getElementById('tbl1').clientHeight}px)`;
            break;
        case 2:
            tbl.style.transform = `translate(-${document.getElementById('tableid').clientWidth}px, -${document.getElementById('tbl1').clientHeight}px)`;
            break;
    }
    let teachersreservations = [];
    for (let i = 0; i < reservations.length; i++) {
        if (!teachersreservations[reservations[i].teacherId]) {
            teachersreservations[reservations[i].teacherId] = [];
        }
        teachersreservations[reservations[i].teacherId][teachersreservations[reservations[i].teacherId].length] = reservations[i];

    }
    console.log("reservations: " + reservations.length);
    for (let i = 0; i < teachersreservations.length; i++) {
        if (teachersreservations[i]) {
            let divname_for_tbl = document.createElement('div');
            divname_for_tbl.style.fontSize = '17px';
            let div_for_tbl = document.createElement('div');
            divname_for_tbl.appendChild(document.createTextNode(teachers[i].fio));
            if (teachers[i].fio.indexOf(value) !== -1 || value === '') {
                for (let j = 0; j < teachersreservations[i].length; j++) {
                    let start = new Date(teachersreservations[i][j].startTime);
                    let end = new Date(teachersreservations[i][j].endTime);
                    let div = document.createElement('div');
                    div.className = 'divtbl';
                    let time = document.createElement('div');
                    time.className = 'divtblatr';
                    time.appendChild(document.createTextNode(start.toLocaleString("ru", {
                        hour: 'numeric',
                        minute: 'numeric'
                    }) + " - " + end.toLocaleString("ru", {
                        hour: 'numeric',
                        minute: 'numeric'
                    })));
                    let name = document.createElement('div');
                    name.appendChild(document.createTextNode(teachersreservations[i][j].classNumber));
                    name.className = 'divtblatr';
                    name.style.textAlign = 'center';
                    let reason = document.createElement('div');
                    reason.appendChild(document.createTextNode(teachersreservations[i][j].reason));
                    reason.className = 'divtblatr';

                    time.style.width = '1000px';
                    time.style.textAlign = 'center';
                    name.style.width = '40%';
                    reason.style.width = '40%';
                    div.appendChild(time);
                    div.appendChild(name);
                    div.appendChild(reason);
                    div_for_tbl.appendChild(div);
                }
                tbl.appendChild(divname_for_tbl);
                tbl.appendChild(div_for_tbl);
            }
        }
    }
}

function CreateTableFirstType() {
    let roomsarr = [];
    let roomsnames = [];
    let j = 1000;
    for (let i = 0; i < reservations.length; i++) {
        if (Number.isInteger(+reservations[i].classNumber)) {
            if (!roomsarr[reservations[i].classNumber]) {
                roomsarr[reservations[i].classNumber] = [];
            }
            roomsarr[reservations[i].classNumber][roomsarr[reservations[i].classNumber].length] = reservations[i];
        } else {
            if (roomsnames.indexOf(reservations[i].classNumber) === -1) {
                roomsnames[j] = [];
                j++;
                roomsarr[j] = [reservations[i]];
            } else {
                roomsarr[roomsnames.indexOf(reservations[i].classNumber)][roomsarr[roomsnames.indexOf(reservations[i].classNumber)].length] = reservations[i];
            }


        }
    }
    console.log("reservations: " + reservations.length);
    let tbl = document.getElementById("tbl1");
    ToKillChildren(tbl);
    for (let i = 0; i < roomsarr.length; i++) {
        if (roomsarr[i]) {
            let divname_for_tbl = document.createElement('div');
            divname_for_tbl.style.fontSize = '17px';
            if (i >= 1000) {
                divname_for_tbl.style.fontSize = '18px';
                divname_for_tbl.style.marginTop = '10px';
            }
            let div_for_tbl = document.createElement('div');
            divname_for_tbl.appendChild(document.createTextNode(roomsarr[i][0].classNumber));
            if (roomsarr[i][0].classNumber.indexOf(value) !== -1 || value === '') {
                for (let j = 0; j < roomsarr[i].length; j++) {
                    let start = new Date(roomsarr[i][j].startTime);
                    console.log(roomsarr[i][j].startTime);
                    let end = new Date(roomsarr[i][j].endTime);
                    let div = document.createElement('div');
                    div.className = 'divtbl';
                    let time = document.createElement('div');
                    time.className = 'divtblatr';
                    time.appendChild(document.createTextNode(start.toLocaleString("ru", {
                        hour: 'numeric',
                        minute: 'numeric'
                    }) + " - " + end.toLocaleString("ru", {
                        hour: 'numeric',
                        minute: 'numeric'
                    })));
                    let name = document.createElement('div');
                    name.appendChild(document.createTextNode(teachers[roomsarr[i][j].teacherId].fio));
                    name.style.paddingLeft = '20px';
                    name.className = 'divtblatr';
                    let reason = document.createElement('div');
                    reason.appendChild(document.createTextNode(roomsarr[i][j].reason));
                    reason.className = 'divtblatr';
                    time.style.width = '1000px';
                    time.style.textAlign = 'center';
                    name.style.width = '40%';
                    reason.style.width = '40%';
                    div.appendChild(time);
                    div.appendChild(name);
                    div.appendChild(reason);
                    div_for_tbl.appendChild(div);
                }
                tbl.appendChild(divname_for_tbl);
                tbl.appendChild(div_for_tbl);
            }
        }
    }
}

function ButtonStyle(button, y) {
    let pruv = false;
    button.style.background = "rgb(23,162,184)";
    button.style.color = 'white';
    button.onmousedown = function () {
        button.style.boxShadow = '0 0 0 3px #eeeeee';
        button.style.color = 'black';
        button.style.background = "rgb(19,136,154)";
        button.style.transition = '0s';
        pruv = true;
    };
    button.onmouseup = function () {
        button.style.background = "rgb(23,162,184)";
        button.style.boxShadow = '0 0 0 3px #3d4752';
        button.style.color = 'white';
        button.style.transition = '0s';
        if (pruv) {
            switch (y) {
                case 0:
                    break;
                case 1:
                    switchView(0);
                    break;
                case 2:
                    switchView(0);
                    Header(true);
                    break;
                case 3:
                    NewReservation();
                    break;
            }
        }
    };
    button.onmouseover = function () {
        button.style.background = "rgb(19,136,154)";
        button.style.boxShadow = '0 0 0 3px #3d4752';
        button.style.transition = '0.6s'
    };
    button.onmouseout = function () {
        button.style.background = "rgb(23,162,184)";
        button.style.boxShadow = '0 0 0 0px #3d4752';
        button.style.color = 'white';
        button.style.transition = '0.6s';
        pruv = false;
    };
}

function NewReservation() {
    let con = new XMLHttpRequest();
    let date1 = document.getElementById('date1');
    con.open('GET', `http://shproj2020.herokuapp.com/get_prs_id`, false);
    con.withCredentials = true;
    con.send();
    let id = +con.response;
    let room_name = document.getElementById('room_name');
    let date = document.getElementById('date');
    let start_time = document.getElementById('start_time');
    let end_time = document.getElementById('end_time');
    let reason = document.getElementById('reason');
    let d1 = StringToDate(date.value, start_time.value);
    let d2 = StringToDate(date.value, end_time.value);
    console.log(`classNumber=${room_name.value}&teacherId=${id}&reason=${reason.value}&
                "startTime"=${d1.getTime()}&endTime=${d2.getTime()}&
                customerId=${id}`);
    con = new XMLHttpRequest();
    con.open('POST', `http://shproj2020.herokuapp.com/reserve`, false);
    con.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    con.withCredentials = true;
    con.send(`classNumber=${room_name.value}&teacherId=${id}&reason=${reason.value}&startTime=${d1.getTime()}&endTime=${d2.getTime()}&customerId=${id}`);
    if (date1.value) {
        reservations = getReservations(StringToDate(date1.value, '00:00').getTime(), 86400000 + StringToDate(date1.value, '00:00').getTime());
        CreateTables();
    } else {
        reservations = getReservations(0, 100000000000000);
        CreateTables();
    }

}

function switchView(i) {
    let conttab = document.getElementById('conttab');
    conttab.style.zIndex = '20';
    let signin = document.getElementById('signin');
    let cont_day = document.getElementById('make_new_res');
    let myres = document.getElementById('myres');
    let y = document.documentElement.clientHeight;
    if (i !== 1 && !isinput) {
        i = 0;
    }
    switch (i) {
        case 0:
            signin.style.transform = 'translate(0, 0)';
            signin.style.transition = '0.6s';
            conttab.style.transform = `translate(0, ${y}px)`;
            conttab.style.transition = '0.6s';
            myres.style.transform = `translate(0, ${y * 2}px)`;
            myres.style.transition = '0.6s';
            cont_day.style.transform = `translate(0, ${y * 3}px)`;
            cont_day.style.transition = '0.6s';
            break;
        case 1:
            signin.style.transform = `translate(0, -${y}px)`;
            signin.style.transition = '0.6s';
            conttab.style.transform = 'translate(0, 0)';
            conttab.style.transition = '0.6s';
            myres.style.transform = `translate(0, ${y}px)`;
            myres.style.transition = '0.6s';
            cont_day.style.transform = `translate(0, ${y * 2}px)`;
            cont_day.style.transition = '0.6s';
            break;
        case 2:
            signin.style.transform = `translate(0, -${2 * y}px)`;
            signin.style.transition = '0.6s';
            conttab.style.transform = `translate(0, -${y}px)`;
            conttab.style.transition = '0.6s';
            myres.style.transform = `translate(0, 0px)`;
            myres.style.transition = '0.6s';
            cont_day.style.transform = `translate(0, ${y}px)`;
            cont_day.style.transition = '0.6s';
            break;
        case 3:
            signin.style.transform = `translate(0, -${3 * y}px)`;
            signin.style.transition = '0.6s';
            conttab.style.transform = `translate(0, -${2 * y}px)`;
            conttab.style.transition = '0.6s';
            myres.style.transform = `translate(0, -${y}px)`;
            myres.style.transition = '0.6s';
            cont_day.style.transform = `translate(0, 0px)`;
            cont_day.style.transition = '0.6s';
            break;
    }
}

function switchTable(i) {
    let tbl1 = document.getElementById('tbl1');
    let tbl2 = document.getElementById('tbl2');
    let tbl3 = document.getElementById('tbl3');
    let tableid = document.getElementById('tableid');
    let x = tableid.clientWidth;
    switch (i) {
        case 0:
            tbl1.style.transition = '0.6s';
            tbl2.style.transition = '0.6s';
            tbl3.style.transition = '0.6s';
            tbl1.style.transform = 'translate(0, 0)';
            tbl2.style.transform = `translate(${x}px, -${document.getElementById('tbl1').clientHeight}px)`;
            tbl3.style.transform = `translate(${x * 2}px, -${document.getElementById('tbl2').clientHeight + document.getElementById('tbl1').clientHeight}px)`;
            break;
        case 1:
            tbl1.style.transition = '0.6s';
            tbl2.style.transition = '0.6s';
            tbl3.style.transition = '0.6s';
            tbl1.style.transform = `translate(-${x}px, 0)`;
            console.log(document.getElementById('tbl1').clientHeight);
            tbl2.style.transform = `translate(0, -${document.getElementById('tbl1').clientHeight}px)`;
            tbl3.style.transform = `translate(${x}px, -${document.getElementById('tbl2').clientHeight + document.getElementById('tbl1').clientHeight}px)`;
            break;
        case 2:
            tbl1.style.transition = '0.6s';
            tbl2.style.transition = '0.6s';
            tbl3.style.transition = '0.6s';
            tbl1.style.transform = `translate(-${x * 2}px, 0)`;
            tbl2.style.transform = `translate(-${x}px, -${document.getElementById('tbl1').clientHeight}px)`;
            tbl3.style.transform = `translate(0, -${document.getElementById('tbl2').clientHeight + document.getElementById('tbl1').clientHeight}px)`;
            break;
    }
}

function ToKillChildren(obj) {
    for (let i = obj.children.length - 1; i > -1; i--) {
        obj.removeChild(obj.children[i]);
    }
}


function StringToDate(s, s0) {
    let d = new Date(s.slice(6), s.slice(3, 5) - 1, s.slice(0, 2), s0.slice(0, 2), s0.slice(3, 5));
    return d;
}
