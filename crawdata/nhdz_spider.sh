#!/bin/sh
#############################################
#                                           #
# Author:dongkai                          #
# Date:2017-05-07                           #
# Describe:内涵段子数据抓取测试       #
#                                           #
#############################################

############# Config init ...... ############

#############################################
date=`date +%Y-%m-%d" "%H":"%M":"%d.%S`

echo "[INFO] 开始日期: $date"

echo "运行参数"
table="mobileportal.rpt_sheet_mobileportal_wap_basic " 
filetype="mp4"
loadtimes="5"
outpath="/root/spider/videos"
mysql_local_file_path="/root/spider/localfiles"
filename=`date +%s`
mysql_local_file="${mysql_local_file_path}/${filename}"

test -e ${outpath}
if [ $? -ne 0 ]
then
    echo "${outpaht} not exists"
	mkdir -p ${outpath}
fi

test -e ${mysql_local_file_path}
if [ $? -ne 0 ]
then
    echo "${mysql_local_file_path} not exists"
	mkdir -p ${mysql_local_file_path}
fi

echo "[INFO] 模块一：抓取数据"

if [ $? -ne 0 ]
then
        exit 255
fi

:<<!
String outputvideo = args[0];
String outputfile = args[1];
int loadtimes = Integer.parseInt(args[2]);
String filetype = args[3];

!

java -jar crawdata.jar ${outpath} ${mysql_local_file} ${loadtimes} ${filetype}>${mysql_local_file_path}/log 2>&1

if [ $? -ne 0 ]
then
        exit 255
fi


echo "[INFO] 模块二："
echo "[INFO] 格式化本地结果文件"

#cat ${logfile_middle_local}${filename}/part1/*|perl -pi -e 's/\001/\t/g'|awk -F "\t" '{print $0}' > ${logfile_middle_local}${filename}/${filename}.txt
#cat ${logfile_middle_local}${filename}/part2/*|perl -pi -e 's/\001/\t/g'|awk -F "\t" '{print $0}' >> ${logfile_middle_local}${filename}/${filename}.txt
#cat ${logfile_middle_local}${filename}/part3/*|perl -pi -e 's/\001/\t/g'|awk -F "\t" '{print $0}' >> ${logfile_middle_local}${filename}/${filename}.txt

if [ $? -ne 0 ]
then
        exit 255
fi

echo "[INFO] 模块三："
echo "[INFO] 调用perl处理本地结果文件"
mysql=`which mysql`
${mysql} -hlocalhost -P3306 -uroot -pcgwxh\$3306 mscshipin<<EOF
load data local infile '${mysql_local_file}' into table ${table};
EOF
if [ $? -ne 0 ]
then
        exit 255
fi

echo "[INFO] 模块四："
echo "[INFO] 删除中间结果文件"

if [ $? -ne 0 ]
then
        exit 255
fi

echo "[INFO] 代码执行结束。"