package com.mke.mword.PDF;



import static com.mke.mword.Database.Constants.DB_APPLICATION_NAME;
import static com.mke.mword.Database.Constants.DB_SAVE_DATA_JSON_FILENAME;
import static com.mke.mword.Database.Constants.DB_SITE_MAIL;
import static com.mke.mword.Database.Constants.DB_SITE_MAIL_PASSWORD;
import static com.mke.mword.Database.Constants.DB_SITE_PDF_PASSWORD;
import static com.mke.mword.Database.Constants.DB_SITE_QUERY_TIME;
import static com.mke.mword.Database.Constants.DB_SITE_QUERY_TIME_NO_WIFI;
import static com.mke.mword.Database.Constants.DB_SITE_THREAD_SLEEP_TIME;
import static com.mke.mword.Database.Constants.DB_VERSION_NUMBER;
import static com.mke.mword.Database.mkDB.nSleepTime;
import static com.mke.mword.Database.mkDB.nVersion;
import static com.mke.mword.Utils.Utils.GetCurrentDate;

import java.io.*;

import com.mke.mword.Database.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


public class PDFGenerator {

    public static String PDF_FILE_NAME_EXT 			= ".pdf";
    public static String PDF_FILE_USER_PASSWORD 	= "";
    public static String PDF_FILE_BOX_NAME_1 		="art";
    public static mkDB m_db=null;

    public PDFGenerator(mkDB db){
        m_db = db;
    }
    public static void addSettingTable(Document document)
    {
        try {
            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);
            Paragraph p = new Paragraph("PROGRAM SETTINGS", f);
            p.setAlignment(Element.ALIGN_CENTER);p.setSpacingBefore(5);p.setSpacingAfter(5);
            document.add(p);
            BaseColor LColor = new BaseColor(201,201,255);
            PdfPTable tableSettings = new PdfPTable(10);
            // set the width of the table to 100% of page
            tableSettings.setWidthPercentage(100);
            // set relative columns width
            tableSettings.setWidths(new float[]{0.4f,0.4f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f});
            tableSettings.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSettings.addCell(createLabelCell(DB_VERSION_NUMBER,LColor));
            tableSettings.addCell(createLabelCell(DB_APPLICATION_NAME,LColor));
            tableSettings.addCell(createLabelCell(DB_SITE_THREAD_SLEEP_TIME,LColor));
            tableSettings.setHeaderRows(1);

            BaseColor VColor = new BaseColor(225,247,213);
            tableSettings.addCell(createValueCell(Double.toString(nVersion),VColor));
            tableSettings.addCell(createValueCell(m_db.stAppName,VColor));
            tableSettings.addCell(createValueCell(Double.toString(nSleepTime),VColor));
            document.add(tableSettings);

            PdfPTable tableSettings1 = new PdfPTable(10);
            tableSettings1.setWidthPercentage(100);
            tableSettings1.setWidths(new float[]{1f,1f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f});
            tableSettings1.addCell(createLabelCell(DB_SITE_QUERY_TIME,LColor));
            tableSettings1.addCell(createLabelCell(DB_SITE_QUERY_TIME_NO_WIFI,LColor));
            tableSettings1.addCell(createLabelCell(DB_SITE_MAIL,LColor));
            tableSettings1.addCell(createLabelCell(DB_SITE_MAIL_PASSWORD,LColor));
            tableSettings1.addCell(createLabelCell(DB_SITE_PDF_PASSWORD,LColor));
            tableSettings1.addCell(createLabelCell("",LColor));
            tableSettings1.addCell(createLabelCell("",LColor));
            tableSettings1.addCell(createLabelCell("",LColor));
            tableSettings1.addCell(createLabelCell("",LColor));
            tableSettings1.addCell(createLabelCell("",LColor));
            tableSettings1.setHeaderRows(1);

            tableSettings1.addCell(createValueCell(m_db.stMail,VColor));
            tableSettings1.addCell(createValueCell(m_db.stMailPassword,VColor));
            tableSettings1.addCell(createValueCell(m_db.stPdfPassword,VColor));
            tableSettings1.addCell(createValueCell("",VColor));
            tableSettings1.addCell(createValueCell("",VColor));
            tableSettings1.addCell(createValueCell("",VColor));
            tableSettings1.addCell(createValueCell("",VColor));
            tableSettings1.addCell(createValueCell("",VColor));
            document.add(tableSettings1);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void addTitlePage(Document document)throws DocumentException {
        Paragraph paragraph = new Paragraph();
        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph("mk Engineering Corp.",
                new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD));//StaticValue.FONT_TITLE);
        // public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("mkRates " +Double.toString(nVersion), new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD));
        //StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
        childParagraph = new Paragraph("Report generated on: "+ GetCurrentDate() ,
                new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD));
        //StaticValue.FONT_SUBTITLE);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
        addEmptyLine(paragraph, 2);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        //End of adding several titles
    }

    /**
     * This method is used to add empty lines in the document
     * @param paragraph
     * @param number
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    // create cells
    private static PdfPCell createLabelCell(String text,BaseColor Color){
        // font
        Font font = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.DARK_GRAY);
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
//        // set style
//        Style.labelCellStyle(cell);
        cell.setBackgroundColor(Color);
        cell.setFixedHeight(15);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    // create cells
    private static PdfPCell createValueCell(String text,BaseColor Color){
        // font
        Font font = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL, BaseColor.BLACK);
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        // set style
//        Style.valueCellStyle(cell);
        cell.setBackgroundColor(Color);
        cell.setFixedHeight(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

//    public static void addBUYArrayTable(Document document) {
//        BaseColor Color =BaseColor.WHITE;
//        try {
//            Font f = new Font();
//            f.setStyle(Font.BOLD);
//            f.setSize(8);
//            Paragraph p = new Paragraph("BUY ALL OPERATIONS", f);
//            p.setAlignment(Element.ALIGN_CENTER);p.setSpacingBefore(5);p.setSpacingAfter(5);
//            document.add(p);
//            PdfPTable tableBUY = new PdfPTable(14);
//            // set the width of the table to 100% of page
//            tableBUY.setWidthPercentage(100);
//            tableBUY.setWidths(new float[] {0.3f,0.35f,1.f,0.9f,0.9f,0.9f,1.1f,1.1f,1.1f,1.1f,0.7f,0.7f,0.7f,0.8f});
//            tableBUY.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            tableBUY.addCell(createLabelCell("No",Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_DATE,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEYCODE,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEYCODE,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEYFLAG,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEYFLAG,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEY,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_CURRENCY,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEY,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_PROFIT,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_ALARMONOFF,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_MAXPROFIT,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_ALARMTYPE,Color));
//            tableBUY.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_REPEATTIME,Color));
//            tableBUY.setHeaderRows(1);
//
//            PdfPCell[] cells1 = tableBUY.getRow(0).getCells();
//            for (int j=0;j<cells1.length;j++){
//                cells1[j].setBackgroundColor(BaseColor.GRAY);
//            }
//
//            for (int i = 0; i < m_db.buyJSONAr.length(); i++) {
//                try {
//                    double dProfit =m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_PROFIT);
//                    if (dProfit>0){Color = new  BaseColor(186,255,201);}
//                    else if (dProfit<0){Color = new BaseColor(255,179,186);}
//                    tableBUY.addCell(createValueCell(Integer.toString(i),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_DATE),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_FROMMONEYCODE),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_TOMONEYCODE),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_FROMMONEYFLAG),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_TOMONEYFLAG),Color));
//                    tableBUY.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_FROMMONEY)),Color));
//                    tableBUY.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_CURRENCY)),Color));
//                    tableBUY.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_TOMONEY)),Color));
//                    tableBUY.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_PROFIT)),Color));
//                    tableBUY.addCell(createValueCell(Boolean.toString(m_db.sellJSONAr.getJSONObject(i).getBoolean(m_db.DB_BUY_SELL_LIST_ALARMONOFF)),Color));
//                    tableBUY.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_MAXPROFIT)),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_ALARMTYPE),Color));
//                    tableBUY.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_REPEATTIME),Color));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            DecimalFormat myFormatter   = new DecimalFormat(  "#.##");
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell(m_db.currencyList.m_stBuyAllFromMoney,Color));
//            tableBUY.addCell(createValueCell(m_db.currencyList.m_stBuyAllMidCur,Color));
//            tableBUY.addCell(createValueCell(m_db.currencyList.m_stBuyAllToMoney,Color));
//            tableBUY.addCell(createValueCell(myFormatter.format(m_db.m_dBuyTotalProfit)+m_db.stToCur,Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//            tableBUY.addCell(createValueCell("-",Color));
//
//            cells1 = tableBUY.getRow(m_db.buyJSONAr.length()+1).getCells();
//            for (int j=0;j<cells1.length;j++){
//                cells1[j].setBackgroundColor(BaseColor.GRAY);
//                cells1[j].setFixedHeight(15);
//            }
//
//            document.add(tableBUY);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void addSELLArrayTable(Document document) {
//        BaseColor Color =BaseColor.WHITE;
//        try {
//            Font f = new Font();
//            f.setStyle(Font.BOLD);
//            f.setSize(8);
//            Paragraph p = new Paragraph("SELL ALL OPERATIONS", f);
//            p.setAlignment(Element.ALIGN_CENTER);p.setSpacingBefore(5);p.setSpacingAfter(5);
//            document.add(p);
//            PdfPTable tableSELL = new PdfPTable(10);
//            tableSELL.setWidthPercentage(100);
//            tableSELL.setWidths(new float[] {0.3f,0.3f,1,1,1,1,0.7f,0.7f,0.7f,0.8f});
//            tableSELL.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            tableSELL.addCell(createLabelCell("No",Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_DATE,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEYCODE,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEYCODE,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEYFLAG,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEYFLAG,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_FROMMONEY,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_CURRENCY,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_TOMONEY,Color));
//            tableSELL.addCell(createLabelCell(m_db.DB_BUY_SELL_LIST_PROFIT,Color));
//            tableSELL.setHeaderRows(1);
//            PdfPCell[] cells1 = tableSELL.getRow(0).getCells();
//            for (int j=0;j<cells1.length;j++){
//                cells1[j].setBackgroundColor(BaseColor.GRAY);
//            }
//            for (int i = 0; i < m_db.sellJSONAr.length(); i++) {
//                try {
//                    double dProfit =((JSONObject)(m_db.sellJSONAr.get(i))).getDouble(m_db.DB_BUY_SELL_LIST_PROFIT);
//                    if (dProfit>0){Color = new  BaseColor(186,255,201);}
//                    else if (dProfit<0){Color = new BaseColor(255,179,186);}
//                    tableSELL.addCell(createValueCell(Integer.toString(i),Color));
//                    tableSELL.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_DATE),Color));
//                    tableSELL.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_FROMMONEYCODE),Color));
//                    tableSELL.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_TOMONEYCODE),Color));
//                    tableSELL.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_FROMMONEYFLAG),Color));
//                    tableSELL.addCell(createValueCell(m_db.sellJSONAr.getJSONObject(i).getString(m_db.DB_BUY_SELL_LIST_TOMONEYFLAG),Color));
//                    tableSELL.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_FROMMONEY)),Color));
//                    tableSELL.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_CURRENCY)),Color));
//                    tableSELL.addCell(createValueCell(Double.toString(m_db.sellJSONAr.getJSONObject(i).getDouble(m_db.DB_BUY_SELL_LIST_TOMONEY)),Color));
//                    tableSELL.addCell(createValueCell(Double.toString(dProfit),Color));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            DecimalFormat myFormatter   = new DecimalFormat(  "#.##");
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell("-",Color));
//            tableSELL.addCell(createValueCell(m_db.currencyList.m_stSellAllFromMoney,Color));
//            tableSELL.addCell(createValueCell(m_db.currencyList.m_stSellAllMidCur,Color));
//            tableSELL.addCell(createValueCell(m_db.currencyList.m_stSellAllToMoney,Color));
//            tableSELL.addCell(createValueCell(myFormatter.format(m_db.m_dSellTotalProfit)+m_db.stToCur,Color));
//
//            cells1 = tableSELL.getRow(m_db.sellJSONAr.length()+1).getCells();
//            for (int j=0;j<cells1.length;j++){
//                cells1[j].setBackgroundColor(BaseColor.GRAY);
//                cells1[j].setFixedHeight(15);
//            }
//
//            document.add(tableSELL);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

    public static File mkDBTopdf() {

        File file =null;
        if (m_db!=null) {
            try {
                PDF_FILE_USER_PASSWORD 	= m_db.stPdfPassword;
                Document document = new Document(PageSize.A4, 10, 10, 15, 5);
                String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                file = new File(path + "/download/" + DB_SAVE_DATA_JSON_FILENAME + PDF_FILE_NAME_EXT);

                if(file.exists()){
                    file.delete();
                }

                FileOutputStream output = null;
                output = new FileOutputStream(file);

                PdfWriter writer = PdfWriter.getInstance(document, output);
                writer.setEncryption(PDF_FILE_USER_PASSWORD.getBytes(), null,
                        PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
                writer.createXmpMetadata();
                writer.setBoxSize(PDF_FILE_BOX_NAME_1, new Rectangle(36, 54, 559, 788));

                document.open();
                document.addCreationDate();
                addTitlePage(document);
    //            document.addTitle(PDF_FILE_TITLE);
                //document.newPage();

                addSettingTable(document);
    //            addBUYArrayTable(document);
    //            addSELLArrayTable(document);

                document.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}