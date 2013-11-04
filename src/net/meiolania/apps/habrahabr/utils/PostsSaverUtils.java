package net.meiolania.apps.habrahabr.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import net.meiolania.apps.habrahabr.data.PostsFullData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostsSaverUtils
{
    private PostsSaverUtils(){}


    public static void deletePost(int position,Context context)
    {
        File file = new File(getSavedPostsPath(context));

        ArrayList<PostsFullData> datas = getSavedPosts(context);
        deleteResources(datas.get(position).getContent(),context);

        datas.remove(position);

        OutputStream fileStream = null;
        ObjectOutputStream objectStream = null;

        if(file.exists())
            file.delete();

        try
        {
            fileStream = new FileOutputStream(file);
            objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(datas);

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fileStream != null)
                try {
                    fileStream.flush();
                    fileStream.close();
                } catch (IOException e) {}
            if(objectStream != null)
                try {
                    objectStream.flush();
                    objectStream.close();
                } catch (IOException e) {}
        }


    }
    public static void savePost(PostsFullData data,Context context)
    {
        File file = new File(getSavedPostsPath(context));

        ArrayList<PostsFullData> datas = getSavedPosts(context);

        data.setContent(saveResources(data.getContent(),context));
        datas.add(data);

        OutputStream fileStream = null;
        ObjectOutputStream objectStream = null;

        if(file.exists())
            file.delete();

        try
        {
            fileStream = new FileOutputStream(file);
            objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(datas);

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fileStream != null)
                try {
                    fileStream.flush();
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(objectStream != null)
                try {
                    objectStream.flush();
                    objectStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public static ArrayList<PostsFullData> getSavedPosts(Context context)
    {
        ArrayList<PostsFullData> list = new ArrayList<PostsFullData>();

        File distinctPath = new File(getSavedPostsPath(context));

        if(!distinctPath.exists())
            return list;


        InputStream outFile = null;
        ObjectInputStream outObject = null;
        try
        {
            outFile = new FileInputStream(distinctPath);
            outObject = new ObjectInputStream(outFile);

            list  = (ArrayList <PostsFullData>)outObject.readObject();

            return list;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return list;
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return list;
        }
        finally
        {
            if(outFile != null)
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(outObject != null)
                try {
                    outObject.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    private static String getSavedPostsPath(Context context)
    {
        String dir =  String.format("/Android/data/%s/files/", context.getPackageName());
        File sd = Environment.getExternalStorageDirectory();
        String filePath = sd.getPath() + dir + "savedPosts.txt";

        new File(sd.getPath() + dir).mkdirs();

        return filePath;
    }
    private static String getResourcesPath(Context context,String url)
    {

        String dir =  String.format("/Android/data/%s/files/", context.getPackageName());
        File sd = Environment.getExternalStorageDirectory();
        File filePath = new File(sd.getPath() + dir + "savedResources");
        filePath.mkdirs();

        return filePath + url.substring(url.lastIndexOf("/"));
    }

    private static void downloadResource(String from,String to)
    {
        OutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        HttpURLConnection connection = null;
        URL url ;
        byte[] buffer = new byte[1024];


        try
        {
            url = new URL(from);

            connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            outputStream = new FileOutputStream(to);

            inputStream = new BufferedInputStream(url.openStream());
            int read ;

            while((read = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer,0,read);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {}
            if(inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {}
            if(connection != null)
                connection.disconnect();
        }
    }
    private static String saveResources(String content,Context context)
    {
        Document document  = Jsoup.parse(content);

        Elements elements = document.getElementsByTag("img");

        for(int i = 0 ; i < elements.size(); i++)
        {
            if(elements.get(i).hasAttr("src"))
            {
                String imageUrl = elements.get(i).attr("src");
                String newPath = getResourcesPath(context,imageUrl);
                content = content.replace(imageUrl,"file://" +newPath);
                downloadResource(imageUrl,newPath);
            }
        }
        return content;
    }
    private static void deleteResources(String content,Context context)
    {
        Document document  = Jsoup.parse(content);

        Elements elements = document.getElementsByTag("img");

        for(int i = 0 ; i < elements.size(); i++)
        {
            if(elements.get(i).hasAttr("src"))
            {
                String imageUrl = elements.get(i).attr("src");
                if(!new File(imageUrl).delete())
                {
                    String newPath = getResourcesPath(context,imageUrl);

                    new File(newPath).delete();
                }

                Log.i("TAG AUDIO","deleted resource "+imageUrl);
            }
        }
    }
}
