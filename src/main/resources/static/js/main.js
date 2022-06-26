let fieldTypeTags = document.querySelectorAll(".fieldType");
let fieldItemTags = document.querySelectorAll(".field-item");

let fromSubmit = document.getElementById("fromSubmit");
let addField = document.getElementById("add-field");

let fieldTypes = null;
let JSONFieldData = null;

let tableData = {
    dbName: null,
    tableName: null,
    dataSize: null,
    fieldData: null
};

window.onload = () => {
    axios.get('/getFieldRule')
        .then(function (response) {
            if (response.status === 200) {
                fieldTypes = response.data;

                //初始化
                // 遍历每一项字段
                fieldItemTags.forEach(fieldItemTag => {

                    // 遍历字段的 HTML 标签
                    fieldItemTag.childNodes.forEach(childNode => {
                        if (!isBlank(childNode.classList)) {

                            // 给 fieldType 标签赋值,初始化
                            if (childNode.classList.contains("fieldType")) {
                                for (let fieldType in fieldTypes) {
                                    let option = document.createElement("option")
                                    option.innerHTML = fieldType
                                    childNode.childNodes[1].appendChild(option)
                                }
                            }

                            // 给 logic 标签赋值,初始化
                            if (childNode.classList.contains("logic")) {
                                for (let fieldType of fieldTypes.int) {
                                    // console.log(fieldType);
                                    let option = document.createElement("option")
                                    option.innerHTML = fieldType
                                    childNode.childNodes[1].appendChild(option)
                                }
                            }
                        }
                    })
                })
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}

let fieldTypeOnclick = (tag) => {

    //从父节点获取，避免 HTML 改动出错
    let parentNode = tag.parentNode.parentNode;
    parentNode.childNodes.forEach(childNode => {
        if (!isBlank(childNode.classList) && childNode.classList.contains("logic")) {
            let logic = childNode
            removeChildNode(logic, "logicSelect");

            logic.childNodes.forEach(x => {
                if (!isBlank(x.classList) && x.classList.contains("logicSelect")) {
                    let logicSelect = x;
                    for (let fieldType of fieldTypes[tag.value]) {
                        let option = document.createElement("option")
                        option.innerHTML = fieldType
                        logicSelect.appendChild(option)
                    }
                }
            })
        }
    });
}

/**
 * 判断 null undefined ""
 * @param obj
 * @returns {boolean}
 */
let isBlank = (obj) => {
    return obj === null || obj === undefined || obj === "";
}

fromSubmit.onclick = (event) => {
    //阻止默认表单提交
    event.preventDefault();
    // checkTableData()
    if (checkFieldData()) {
        console.log("cccc")
        summitData()
    }
}

addField.onclick = (event) => {
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
        // console.log("string");
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

let checkTableData = () => {
    let dataBaseName = document.getElementById("database_name").value;
    let tableName = document.getElementById("table_name").value;
    let tableSize = document.getElementById("table_size").value;

    // console.log(tableSize);
    if (checkName(dataBaseName, "数据库名") && checkName(tableName, "表名")) {

    } else {
        return false;
    }
}

let checkFieldData = () => {
    let allFieldNameInput = document.querySelectorAll(".fieldNameInput");
    let allFieldType = document.querySelectorAll(".fieldTypeSelect");
    let allLogicSelect = document.querySelectorAll(".logicSelect");
    let allPrefixInput = document.querySelectorAll(".prefixInput");
    let dataLength = allFieldNameInput.length;
    // console.log(Object.prototype.toString.call(allFieldNameInput));
    if (!checkName(allFieldNameInput, "字段名")) return false;

    let fieldDataArray = new Array();
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
        fieldDataArray[i] = tempData
    }

    // tableData.fieldData = JSON.stringify(fieldDataArray);
    tableData.fieldData = fieldDataArray
    console.log(tableData.fieldData);
    return true
}


let summitData = () => {
    // if (isBlank(JSONFieldData)) return false
    console.log("summitData：" + tableData.fieldData)
    axios({
        method: "post",
        url: "/rules",
        headers: {
            'Content-Type': 'application/json'
        },
        // data: tableData.fieldData
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
            // console.log("remove childNodes :" + i)
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