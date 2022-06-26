// import {axios} from "./axios";
//
// let fieldTypeTags = document.querySelectorAll(".fieldType");
// let logicTags = document.querySelectorAll(".logic");
// let fieldItemTags = document.querySelectorAll(".fieldItem");
// let fromSubmit = document.getElementById("from_submit");
//
// let fieldTypes = null;
//
// window.onload = () => {
//     axios.get('/getFieldRule')
//         .then(function (response) {
//             if (response.status === 200) {
//                 fieldTypes = response.data;
//                 // console.log(fieldTypes.int);
//
//                 //初始化
//                 // 遍历每一项字段
//                 fieldItemTags.forEach(fieldItemTag => {
//
//                     // 遍历字段的 HTML 标签
//                     fieldItemTag.childNodes.forEach(childNode => {
//                         if (!isBlank(childNode.classList)) {
//
//                             // 给 fieldType 标签赋值,初始化
//                             if (childNode.classList.contains("fieldType")) {
//                                 for (let fieldType in fieldTypes) {
//                                     let option = document.createElement("option")
//                                     option.innerHTML = fieldType
//                                     childNode.childNodes[1].appendChild(option)
//                                 }
//                             }
//
//                             // 给 logic 标签赋值,初始化
//                             if (childNode.classList.contains("logic")) {
//                                 for (let fieldType of fieldTypes.int) {
//                                     // console.log(fieldType);
//                                     let option = document.createElement("option")
//                                     option.innerHTML = fieldType
//                                     childNode.childNodes[1].appendChild(option)
//                                 }
//                             }
//
//                         }
//                     })
//                 })
//             }
//         })
//         .catch(function (error) {
//             console.log(error);
//         });
// }
//
//
// fieldTypeTags[0].onclick = () => {
//     console.log(fieldTypeTags[0].childNodes[1].value);
// }
//
// let isBlank = (obj) => {
//     return obj === null || obj === undefined || obj === "";
// }
//
// fromSubmit.onclick = (event) => {
//     //阻止默认表单提交
//     event.preventDefault();
//     everyFieldInfo();
// }
//
// let everyFieldInfo = () => {
//     let allFieldNameInput = document.querySelectorAll(".fieldNameInput");
//     let allFieldType = document.querySelectorAll(".fieldTypeSelect");
//     let allLogicSelect = document.querySelectorAll(".logicSelect");
//     let allPrefixInput = document.querySelectorAll(".prefixInput");
//     let dataLength = allFieldNameInput.length;
//
//     let fieldDataArray = new Array();
//     for (let i = 0; i < dataLength; i++) {
//         let tempData = Object.call(null)
//         tempData[allFieldNameInput[i].name] = allFieldNameInput[i].value;
//         tempData[allFieldType[i].name] = allFieldType[i].value;
//         tempData[allLogicSelect[i].name] = allLogicSelect[i].value;
//         tempData[allPrefixInput[i].name] = allPrefixInput[i].value;
//         fieldDataArray[i] = tempData
//     }
//
//     let JSONFieldData = JSON.stringify(fieldDataArray);
//     console.log(fieldDataArray);
//     console.log(JSONFieldData);
//
//     axios({
//         method: "post",
//         url: "/rules",
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         data: JSONFieldData
//     })
//         .then((response) => {
//             console.log(response);
//         })
//         .catch((error) => {
//             console.log(error);
//         });
// }
