let fieldTypeTags = document.querySelectorAll(".fieldType");
let fieldItemTags = document.querySelectorAll(".field-item");

let fieldTypes = null;

let tableData = {
    dataBaseName: null,
    tableName: null,
    dataSize: null,
    fieldData: null
};

let connectionStatus = false;

/**
 * HTML 加载完毕后做以下的事情
 *
 * 1.检查是否已经连接数据库
 * 2.从后端获取字段生成配置
 * 3.给每项字段[数据类型] [生成逻辑]赋值选项
 */
window.onload = () => {
    // 判断是否已经连接
    axios({
        method: "get",
        url: "/dataBaseConnection"
    })
        .then((response) => {
            console.log(response);
            if (response.data === true) {
                let connectionStatusTag = document.getElementById("connection-status");
                connectionStatusTag.innerHTML = "数据库已连接"
                connectionStatusTag.style.color = "#006ed3"
                connectionStatus = true;
            }
        })
        .catch((error) => {
            console.log(error);
        });

    axios.get('/getFieldRule')
        .then(function (response) {
            if (response.status === 200) {
                fieldTypes = response.data;

                // 初始化 遍历页面每一项字段
                fieldItemTags.forEach(fieldItemTag => {
                    for (let fieldTypeSelect of fieldItemTag.getElementsByClassName("fieldTypeSelect")) {
                        for (let fieldType in fieldTypes) {
                            let option = document.createElement("option")
                            option.innerHTML = fieldType
                            fieldTypeSelect.appendChild(option)
                        }
                    }
                    for (let logicSelect of fieldItemTag.getElementsByClassName("logicSelect")) {
                        for (let fieldType of fieldTypes.int) {
                            let option = document.createElement("option")
                            option.innerHTML = fieldType
                            logicSelect.appendChild(option)
                        }
                    }
                })
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}

/**
 * 切换数据类型时，生成逻辑也会随之切换
 * @param tag
 */
let fieldTypeOnclick = (tag) => {

    // TODO 获取有更好的写法
    //从父节点获取，避免 HTML 改动出错
    let parentNode = tag.parentNode.parentNode;
    parentNode.childNodes.forEach(childNode => {

        if (!isBlank(childNode.classList) && childNode.classList.contains("logic")) {
            let logic = childNode
            removeChildNode(logic, "logicSelect");

            logic.childNodes.forEach(x => {
                if (!isBlank(x.classList) && x.classList.contains("logicSelect")) {
                    for (let fieldType of fieldTypes[tag.value]) {
                        let option = document.createElement("option")
                        option.innerHTML = fieldType
                        x.appendChild(option)
                    }
                }
            })
        }
    })
}

/**
 * 判断 null undefined ""
 * @param obj
 * @returns {boolean}
 */
let isBlank = (obj) => {
    return obj === null || obj === undefined || obj === "";
}

/**
 * 提交数据
 * 提交前会对 连接状态 / 数据名 / 表名 / 字段规范进行检查
 * @param event
 */
let formSubmit = (event) => {
    //阻止默认表单提交
    event.preventDefault();

    if (!connectionStatus) {
        alert("请先连接数据库")

    } else if (checkFieldData() && checkTableData()) {
        console.log("formSubmit")
        summitData()
    }
}

/**
 *  TODO 应该使用隐藏的 tr 来复制
 * 添加字段
 * @param event
 */
let addField = (event) => {
    //阻止默认表单提交
    event.preventDefault();
    //克隆 / 复制 添加一个节点
    let node = fieldItemTags[0].cloneNode(true);
    fieldItemTags[0].parentNode.append(node);
}

/**
 * 判断是否存在中文字符
 * @param str
 * @returns {boolean}
 */
let isChineseChar = (str) => {
    const reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/
    return reg.test(str)
};

/**
 * 名字校验
 * @param nameList 名字List
 * @param objName 所属标签，为空时，Alter不显示标签名
 * @returns {boolean}
 */
let checkName = (nameList, objName) => {
    if (isBlank(objName)) objName = "";
    if (typeof nameList == "string") {
        nameList = new Array({value: nameList})
    }

    let checkNameRepeat = new Set();
    for (let tag of nameList) {
        if (isBlank(tag.value)) {
            alert(objName + "不允许为空")
            return false;
        }
        if (isChineseChar(tag.value)) {
            alert(objName + "不允许存在中文")
            return false;
        }
        checkNameRepeat.add(tag.value)
    }

    if (checkNameRepeat.size !== nameList.length) {
        alert("不允许" + objName + "重复")
        return false;
    }
    return true
}

/**
 * 检查 数据库名，表名，是否正确
 * 并设置值
 * @returns {boolean}
 */
let checkTableData = () => {
    let dataBaseName = document.getElementById("database-name").value;
    let tableName = document.getElementById("table-name").value;
    let tableSize = document.getElementById("table-size").value;

    if (checkName(dataBaseName, "数据库名") && checkName(tableName, "表名")) {
        tableData.dataBaseName = dataBaseName;
        tableData.tableName = tableName;
        tableData.dataSize = tableSize;
        return true
    } else {
        // alert("请填写正确数据库名和表名")
        return false;
    }
}

/**
 * 检查各字段是否正确，并设置值
 * @returns {boolean}
 */
let checkFieldData = () => {
    let allPrimary = document.querySelectorAll(".primary-checkbox");

    allPrimary.forEach(x => {
            console.log(x.checked)
            console.log(x.name)
        }
    )
    let allFieldNameInput = document.querySelectorAll(".fieldNameInput");
    let allFieldType = document.querySelectorAll(".fieldTypeSelect");
    let allLogicSelect = document.querySelectorAll(".logicSelect");
    let allPrefixInput = document.querySelectorAll(".prefixInput");
    let dataLength = allFieldNameInput.length;
    if (!checkName(allFieldNameInput, "字段名")) return false;

    let fieldDataArray = [];
    for (let i = 0; i < dataLength; i++) {
        if (allFieldType[i].value === "int" && isNaN(allPrefixInput[i].value)) {
            alert("数据类型与前缀不合符")
            return false;
        }
        if ((allFieldType[i].value === "date" && !isBlank(allPrefixInput[i].value)) ||
            (allLogicSelect[i].value === "自增" && !isBlank(allPrefixInput[i].value))) {
            alert("不允许填写前缀")
            return false;
        }
        if (allLogicSelect[i].value === "仅前缀" && isBlank(allPrefixInput[i].value)) {
            alert("请填写前缀")
            return false;
        }

        let tempData = Object.call(null)
        tempData[allFieldNameInput[i].name] = allFieldNameInput[i].value;
        tempData[allFieldType[i].name] = allFieldType[i].value;
        tempData[allLogicSelect[i].name] = allLogicSelect[i].value;
        tempData[allPrefixInput[i].name] = allPrefixInput[i].value;
        tempData[allPrimary[i].name] = allPrimary[i].checked;

        fieldDataArray[i] = tempData
    }
    tableData.fieldData = fieldDataArray
    console.log(tableData.fieldData);
    return true
}


let summitData = () => {
    console.log(tableData.fieldData)
    axios({
        method: "post",
        url: "/rules",
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(tableData)
    })
        .then((response) => {
            // console.log(response);
        })
        .catch((error) => {
            console.log(error);
        });
}

/**
 * @param node 处于哪个超节点下
 * @param parenClassName 需要移除哪个父节点下的子节点class名，为空时移除所有
 *
 */
let removeChildNode = (node, parenClassName) => {
    let childNodes = node.childNodes;
    if (childNodes.length <= 0) {
        // console.log("无子节点")
        return;
    }

    // removeAllChildNode
    if (isBlank(parenClassName)) {
        for (let i = childNodes.length - 1; i >= 0; i--) {
            node.removeChild(childNodes[i])
        }
        return;
    }

    // removeByParenClassName
    for (let i = childNodes.length - 1; i >= 0; i--) {
        if (!isBlank(childNodes[i].classList) && childNodes[i].classList.contains(parenClassName)) {
            removeChildNode(childNodes[i])
            return;
        }
        removeChildNode(childNodes[i], parenClassName)
    }
}

let connectionSubmit = (event) => {
    event.preventDefault();
    let url = document.getElementById("dataBase-url").value;
    let username = document.getElementById("dataBase-username").value;
    let password = document.getElementById("dataBase-password").value;
    let method = document.getElementById("connection-method").value;
    let saveConnectionInfo = document.getElementById("save-connection-info").checked;

    let connectInfo = new FormData();
    connectInfo.append("url", url)
    connectInfo.append("username", username)
    connectInfo.append("password", password)
    console.log(connectInfo);
    axios({
        method: "post",
        url: "/dataBaseConnection",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: connectInfo
    })
        .then((response) => {
            if (response.data.success) {
                alert(response.data.data)
                //存储登录信息到本地
                if (saveConnectionInfo) {
                    localStorage.setItem("url", url);
                    localStorage.setItem("username", username);
                    localStorage.setItem("password", password);
                }

            } else {
                alert(response.data.errorMsg)
                return;
            }
            console.log(response);
        })
        .catch((error) => {
            // alert("连接错误，请检查连接信息")
            console.log(error);
        });
}

let connectionInfoRecord = (event) => {
    event.preventDefault();
    let item = localStorage.getItem("url");
    if (isBlank(item)) {
        alert("没有相关登录信息")
        return;
    }
    document.getElementById("dataBase-url").value = item;
    document.getElementById("dataBase-username").value = localStorage.getItem("username");
    document.getElementById("dataBase-password").value = localStorage.getItem("password");
}