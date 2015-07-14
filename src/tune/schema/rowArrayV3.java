package tune.schema;

import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.exec.RecordReader;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.LineRecordReader.LineReader;
import org.apache.hadoop.io.Text;

import tune.schema.rawLog;

/**
 * 
 * @author fuat
 *
 */
@SuppressWarnings("deprecation")
public class rowArrayV3 implements RecordReader{

	private InputStream fileInputStream;
	private Configuration conf;
	private Properties pro;
	private LineReader lineReader;
	private Text row;
	
	/*public static void main(String[] args) throws FileNotFoundException,
			IOException {
		String directory = "/home/fuat/2015/03/21/shard14_20150321_rev0_prison00_batcher00_seq0.fb.gz";
		//readStreamBufferByBuffer(directory);

	}*/

	

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 *//*
	private static BytesWritable readStreamBufferByBuffer(java.io.InputStream fileInputStream) throws IOException,
			FileNotFoundException {


			if (fileInputStream.available() <= 0)
				System.exit(0);
			
			byte[] bs = new byte[4];

			Integer readBytes = 0;
			while (4 - readBytes > 0) {
				readBytes += fileInputStream.read(bs, readBytes, 4 - readBytes);
			}

			ByteBuffer bff2 = ByteBuffer.wrap(bs);
			int position1 = bff2.getInt();
	

			byte[] bs2 = new byte[position1];

			Integer bytesRead = 0;
			while (position1 - bytesRead > 0) {
				bytesRead += fileInputStream.read(bs2, bytesRead, position1 - bytesRead);
			}


		return  new BytesWritable(bs2);
		
	}

	*/



	public void close() throws IOException {
		fileInputStream.close();
		
	}

	public Writable createRow() throws IOException {
		
		
		
		if (fileInputStream.available() <= 0)
			System.exit(0);
		
		byte[] bs = new byte[4];

		Integer readBytes = 0;
		while (4 - readBytes > 0) {
			readBytes += fileInputStream.read(bs, readBytes, 4 - readBytes);
		}

		ByteBuffer bff2 = ByteBuffer.wrap(bs);
		int position1 = bff2.getInt();


		byte[] bs2 = new byte[position1];

		Integer bytesRead = 0;
		while (position1 - bytesRead > 0) {
			bytesRead += fileInputStream.read(bs2, bytesRead, position1 - bytesRead);
		}
		
		
		
		
		ByteBuffer bb = ByteBuffer.wrap(bs2);

		rawLog rlog = tune.schema.rawLog.getRootAsrawLog(bb);
		String s  = rlog.advertiserId() + "  " + rlog.created() + " "
				+ rlog.clickCreated() + "  " + rlog.advertiserSubAdgroup()
				+ " " + rlog.attributedIdDate();

		row = new Text(s);
	return  row;
		
	}

	public void initialize(InputStream fileInputStream, Configuration conf, Properties pro)
			throws IOException {
		//this.fileInputStream = fileInputStream;
		lineReader = new LineReader(fileInputStream, conf);
		this.conf = conf;
		this.pro = pro;
		
	}

	public int next(Writable row) throws IOException {
		

		if(lineReader == null)
			return -1;
		
		return lineReader.readLine((Text) row);
	}
}

