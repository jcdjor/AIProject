// 服务器主地址
var basePath="http://127.0.0.1:8080/AIProject"
// 背景效果
$(document).ready(function() {
  $('#context').particleground({
    dotColor: '#5cbdaa',
    lineColor: '#5cbdaa'
  });
  $('.intro').css({
    'margin-top': -($('.intro').height() / 2)
  });
});