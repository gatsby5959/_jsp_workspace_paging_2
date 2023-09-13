package domain;

//CREATE TABLE `board` (
//		  `bno` int NOT NULL AUTO_INCREMENT,
//		  `title` varchar(200) NOT NULL,
//		  `writer` varchar(100) NOT NULL,
//		  `content` text,
//		  `regdate` datetime DEFAULT CURRENT_TIMESTAMP,
//		  `moddate` datetime DEFAULT CURRENT_TIMESTAMP,
//		  `readcount` int DEFAULT '0',
//		  PRIMARY KEY (`bno`)
//		) 
public class BoardVO {
	private int bno;
	private String title;
	private String writer;
	private String content;
	private String regdate;
	private String moddate;
	
	//생성자
	public BoardVO() {
		
	}

	//insert
	public BoardVO(String title, String writer, String content) {
		super();
		this.title = title;
		this.writer = writer;
		this.content = content;
	}

	//list
	public BoardVO(int bno, String title, String writer, String regdate) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.regdate = regdate;
	}
	
	//update
	public BoardVO(int bno, String title, String content) {
		super();
		this.bno = bno;
		this.title = title;
		this.content = content;
	}

	//detail전부다
	public BoardVO(int bno, String title, String writer, String content, String regdate, String moddate) {
		super();
		this.bno = bno;
		this.title = title;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.moddate = moddate;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getModdate() {
		return moddate;
	}

	public void setModdate(String moddate) {
		this.moddate = moddate;
	}

	@Override
	public String toString() {
		return "BoardVO [bno=" + bno + ", title=" + title + ", writer=" + writer + ", content=" + content + ", regdate="
				+ regdate + ", moddate=" + moddate + "]";
	}
	
	
	
}
