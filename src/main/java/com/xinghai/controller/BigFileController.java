package com.xinghai.controller;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author HeWei
 */
@Controller
@RequestMapping("/bigfile")
public class BigFileController {


    private final static String utf8 ="utf-8";
    @RequestMapping("/upload")
    @ResponseBody
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //分片
        response.setCharacterEncoding(utf8);
        Integer schunk = null;
        Integer schunks = null;
        String name = null;
        String uploadPath = "D:\\home\\fileItem";
        BufferedOutputStream os = null;
        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024);
            factory.setRepository(new File(uploadPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(5L * 1024L * 1024L * 1024L);
            upload.setSizeMax(10L * 1024L * 1024L* 1024L);
            List<FileItem> items = upload.parseRequest(request);

            for(FileItem item : items){
                if(item.isFormField()){
                    System.out.println("--->item.isFormField()");
                    if("chunk".equals(item.getFieldName())){
                        schunk = Integer.parseInt(item.getString(utf8));
                    }
                    if("chunks".equals(item.getFieldName())){
                        schunks = Integer.parseInt(item.getString(utf8));
                    }
                    if("name".equals(item.getFieldName())){
                        name = item.getString(utf8);
                    }
                }
            }
            for(FileItem item : items){
                if(!item.isFormField()){
                    System.out.println("--->!item.isFormField()");
                    String temFileName = name;
                    if(name != null){
                        if(schunk != null){
                            temFileName = schunk +"_"+name;
                        }
                        File temFile = new File(uploadPath,temFileName);
                        if(!temFile.exists()){//断点续传
                            item.write(temFile);
                        }
                    }
                }
            }
            //文件合并
            if(schunk != null && schunk.intValue() == schunks.intValue()-1){
                System.out.println("--->schunk != null && schunk.intValue() == schunks.intValue()-1");

                File tempFile = new File(uploadPath,name);
                os = new BufferedOutputStream(new FileOutputStream(tempFile));

                for(int i=0 ;i<schunks;i++){
                    File file = new File(uploadPath,i+"_"+name);
                    while(!file.exists()){
                        Thread.sleep(100);
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    os.write(bytes);
                    os.flush();
                    file.delete();
                }
                os.flush();
            }
            response.getWriter().write("上传成功"+name);
        }finally {
            try{
                if(os != null){
                    os.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/download")
    public void downLoadFile(HttpServletRequest request,HttpServletResponse response) throws Exception{
        File file = new File("D:\\home\\fileItem\\ideaIU-2019.2.4.exe");
        response.setCharacterEncoding(utf8);

        //输入输出对象
        InputStream is = null;
        OutputStream os = null;
        try{
            //分片下载   http  Range bytes=100-1000   bytes=100-
            long fSize = file.length();
            response.setContentType("application/x-download");
            String fileName = URLEncoder.encode(file.getName(),utf8);
            response.addHeader("Content-Disposition","attachment;filename=" + fileName);
            response.setHeader("Accept-Range","bytes");

            response.setHeader("fSize",String.valueOf(fSize));
            response.setHeader("fName",fileName);

            long pos = 0,last = fSize-1,sum = 0;
            if(null != request.getHeader("Range")){
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                String numRange = request.getHeader("Range").replaceAll("bytes=","");
                String[] strRange = numRange.split("-");
                if(strRange.length == 2){
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    if(last > fSize-1){
                        last = fSize-1;
                    }
                }else{
                    pos = Long.parseLong(numRange.replaceAll("-","").trim());
                }
            }
            long rangeLenght = last - pos +1;
            String contentRange = new StringBuffer("bytes ").append(pos).append("-").append(last).append("/").append(fSize).toString();
            response.setHeader("Content-Range",contentRange);
            response.setHeader("Content-Lenght",String.valueOf(rangeLenght));

            os = new BufferedOutputStream(response.getOutputStream());
            is = new BufferedInputStream(new FileInputStream(file));
            is.skip(pos);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while(sum < rangeLenght){
                lenght = is.read(buffer,0,((rangeLenght-sum) <= buffer.length ? ((int)(rangeLenght-sum)) :  buffer.length));
                sum = sum+ lenght;
                os.write(buffer,0,lenght);
            }
            System.out.println("下载完成");
        }finally {
            if(is != null){
                is.close();
            }
            if(os != null){
                os.close();
            }
        }
    }


    /*@RequestMapping(value = "/upload2",method = RequestMethod.POST)
    @ResponseBody
    public Results upload2(HttpServletRequest request, HttpServletResponse response) throws FileUploadException {

        return Results.success();
    }

    @Autowired
    BigFileService bigFileService;


    @GetMapping("/open")
    public ModelAndView open() {

        return new ModelAndView("bigfile_1");
    }

    @PostMapping("/isUpload")
    public Map<String, Object> isUpload(FileForm form) {

        return bigFileService.findByFileMd5(form.getMd5());

    }


    @GetMapping("/upload4")
    @PreAuthorize("hasAnyAuthority('sys:file:up')")
    public Results upload(){
        return null;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload( FileForm form,@RequestParam(value = "data", required = false) MultipartFile multipartFile) {
        Map<String, Object> map = null;

        try {
            map = bigFileService.realUpload(form, multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    @RequestMapping(value = "/upload1",method = RequestMethod.POST)
    @ResponseBody
    public Results upload1(HttpServletRequest request, HttpServletResponse response) throws FileUploadException {
        //分片
        response.setCharacterEncoding(UTF8);
        //当前分片数
        Integer schunk = null;
        //总分片数
        Integer schunks = null;
        //文件名
        String name = null;
        //文件临时路径
        String uploadPath = "D:\\fileItem";

        BufferedOutputStream os = null;
        try {

            DiskFileItemFactory factory = new DiskFileItemFactory();
            //缓冲区，先督导内存，然后写到本地
            factory.setSizeThreshold(1024);
            //设置临时目录
            factory.setRepository(new File(uploadPath));
            //解析request
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(5L * 1024 * 1024 * 1024);
            upload.setSizeMax(10L * 1024 * 1024 * 1024);
            List<FileItem> items = upload.parseRequest((RequestContext) request);

            for (FileItem item : items){
                //判断是否为文件
                if (item.isFormField()){
                    if ("chunk".equals(item.getFieldName())){
                        schunk = Integer.parseInt(item.getString(UTF8));
                    }

                    if ("chunks".equals(item.getFieldName())){
                        schunks = Integer.parseInt(item.getString(UTF8));
                    }

                    if ("name".equals(item.getFieldName())){
                        name = item.getString(UTF8);
                    }
                }
            }

            for (FileItem item : items){
                //判断是否为文件
                if (item.isFormField()){
                    String temFileName = name;
                    if (null != name){
                        if (null != schunk){
                            temFileName = schunk + "_" + name;
                        }
                        File temFile = new File(uploadPath,temFileName);
                        //断点续传
                        if (temFile.exists()){
                            item.write(temFile);
                        }
                    }
                }
            }

            //文件合并
            //在最后一个分片时才合并
            if (null != schunk && schunk.intValue() == schunks.intValue() -1){
                File tempFile = new File(uploadPath,name);
                os = new BufferedOutputStream(new FileOutputStream(tempFile));

                for (int i = 0 ; i < schunks; i++){
                    File file = new File(uploadPath,i + "_" + name);
                    while (!file.exists()){
                        Thread.sleep(100);
                    }

                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    os.write(bytes);
                    os.flush();

                    //删除临时文件
                    file.delete();
                }

                os.flush();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return Results.success();
    }
*/


}
