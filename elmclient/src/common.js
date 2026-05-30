export default {
    getCurDate() {
        let date = new Date();
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        month = month < 10 ? "0" + month : month;
        day = day < 10 ? "0" + day : day;
        return year + "-" + month + "-" + day;
    },
    getSessionStorage(key) {
        let str = sessionStorage.getItem(key);
        if (str == '' || str == null || str == 'null' || str == undefined) {
            return null;
        } else {
            return JSON.parse(str);
        }
    },
    setSessionStorage(key, val) {
        sessionStorage.setItem(key, JSON.stringify(val));
    },
    removeSessionStorage(key) {
        sessionStorage.removeItem(key);
    },
    getLocalStorage(key) {
        let str = localStorage.getItem(key);
        if (str == '' || str == null || str == 'null' || str == undefined) {
            return null;
        } else {
            return JSON.parse(str);
        }
    },
    setLocalStorage(key, val) {
        localStorage.setItem(key, JSON.stringify(val));
    },
    removeLocalStorage(key) {
        localStorage.removeItem(key);
    }
}
