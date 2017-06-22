var dictionary={};
var dictionaryAll={};
  //获取字典项
axios.get('/keyValue/items/hangupPosition_sex_isRepeate_followStatus_callResult').then(function (response) {
    //console.log(response.data);
    dictionary.hangUpPosition=cyclic(response.data[0].keyValueItems);//保存有效的挂断位置
    dictionary.sex=response.data[1].keyValueItems;//性别
    dictionary.isRepeate=response.data[2].keyValueItems;//标签是否重复
    dictionary.followStatus=cyclic(response.data[3].keyValueItems);//保存有效的跟踪状态
    dictionary.callResult=cyclic(response.data[4].keyValueItems);//保存有效的呼叫结果

    dictionaryAll.hangUpPosition=response.data[0].keyValueItems;//保存所有的挂断位置
    dictionaryAll.sex=response.data[1].keyValueItems;//性别
    dictionaryAll.isRepeate=response.data[2].keyValueItems;//标签是否重复
    dictionaryAll.followStatus=response.data[3].keyValueItems;//保存所有的挂断位置
    dictionaryAll.callResult=response.data[4].keyValueItems;//保存所有的呼叫结果
    //console.log(dictionary)
    //console.log(dictionaryAll)
  }).catch(function (error) {
    console.log(error);
  });
function cyclic(obj){
  var arr1=[];
  for(var i in obj){
    if(!obj[i].status){
      arr1.push(obj[i]);
    }
  }
  return arr1;

}
function changeVal(val,item){
  for( var j=0;j<dictionaryAll[item].length;j++){
    if(val == dictionaryAll[item][j].code ){
      val = dictionaryAll[item][j].name;
    }
  }
  return val;
}


