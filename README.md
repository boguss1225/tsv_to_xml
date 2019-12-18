# tsv_to_xml
convert tsv to xml for tensorflow object detection Anotation labeling

# Description
This program can convert multiple tsv file to multiple xml files.\
tsv file has to be devided into two files.
* *.bboxes.labels.tsv     -> files contain label name details
* *.bbixes.tsv            -> files contain position details (xmin ymin xmax ymax)
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture4.PNG)
\
contents of a pair of tsv files.

# Usage
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture1.PNG)
\

* 3 files (.jpg & .bboxes.labels.tsv & .bboxes.tsv) has to be in the same folder before convert.
* image file format has to be "jpg"
* output path for xml files is same as source folder
\
\
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture0.PNG)
\
Initial screen of the program
\
\
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture01.PNG)
\
conversion finished screen
\
\
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture3.PNG)
\
conversion finished XML
\
\
![picture](https://github.com/boguss1225/tsv_to_xml/blob/master/ScreenShots/capture2.PNG)
\
saved XML

