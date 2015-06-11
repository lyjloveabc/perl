
$temp=qq({"totalProperty":"59","root":[{"createTime":"2015-06-05 11:33:49","id":"188","modifyTime":"2015-06-05 11:33:49","name":"关于我们","price":"2","ringId":"810033540449","singer":"周笔畅","type":"1","url":"http:\/\/61.132.134.128\/colorring\/wav\/sys\/1750385.wav"}]});
$temp=~s/"/'/g;
print $temp;