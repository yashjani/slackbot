
package com.openai.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFBackground;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.springframework.stereotype.Service;

import com.openai.slack.event.dto.SlideData;

@Service
public class PowerPointCreator {

	static {
		// Set a higher override value for the maximum allowable size
		IOUtils.setByteArrayMaxOverride(1000 * 1024 * 1024); // 100 MB
	}

	public String createPresentation(List<SlideData> slideDataList) throws IOException {
		XMLSlideShow ppt = new XMLSlideShow();

		for (SlideData slideData : slideDataList) {
			createSlide(ppt, slideData);
		}
		String outputFilePath = "output.pptx"; // Specify the output file path
		// Save the presentation
		try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
			ppt.write(out);
		}
		ppt.close();
		return outputFilePath;
	}

	private void createSlide(XMLSlideShow ppt, SlideData slideData) throws IOException {
		// Create a blank slide layout
		XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
		XSLFSlideLayout blankLayout = defaultMaster.getLayout(SlideLayout.BLANK);
		XSLFSlide slide = ppt.createSlide(blankLayout);

		// Set the background color of the slide to white (optional)
		XSLFBackground background = slide.getBackground();
		background.setFillColor(Color.WHITE);

		// Define dimensions
		Dimension pgsize = ppt.getPageSize();

		// Add title text box
		double left = 0.5 * 72; // 0.5 inches
		double top = 0.5 * 72; // 0.5 inches
		double width = pgsize.getWidth() - 1 * 72; // full width minus 1 inch
		double height = 1 * 72; // 1 inch

		XSLFTextBox titleBox = slide.createTextBox();
		titleBox.setAnchor(new Rectangle2D.Double(left, top, width, height));
		XSLFTextParagraph titleP = titleBox.addNewTextParagraph();
		titleP.setTextAlign(TextAlign.CENTER);
		XSLFTextRun r1 = titleP.addNewTextRun();
		r1.setText(slideData.getTitle());
		r1.setFontSize(24.0);
		r1.setBold(true);
		r1.setFontColor(Color.BLACK);

		// Add text box
		top = 1.5 * 72; // 1.5 inches
		height = 2 * 72; // 2 inches

		XSLFTextBox textBox = slide.createTextBox();
		textBox.setAnchor(new Rectangle2D.Double(left, top, width, height));
		XSLFTextParagraph textP = textBox.addNewTextParagraph();
		textP.setTextAlign(TextAlign.LEFT);
		XSLFTextRun r2 = textP.addNewTextRun();
		r2.setText(slideData.getText());
		r2.setFontSize(18.0);
		r2.setFontColor(Color.BLACK);

		// Add image
		if (slideData.getImagePath() != null && !slideData.getImagePath().isEmpty()) {
			File imageFile = new File(slideData.getImagePath());
			if (imageFile.exists()) {
				XSLFPictureData pd = ppt.addPicture(imageFile, XSLFPictureData.PictureType.JPEG);
				width = pgsize.getWidth() - 1 * 72; // full width minus 1 inch
				height = pgsize.getHeight() - 4 * 72; // height minus margins for title and text
				left = 0.5 * 72; // 0.5 inches
				top = 3.5 * 72; // 3.5 inches

				slide.createPicture(pd).setAnchor(new Rectangle2D.Double(left, top, width, height));
			}
		}
	}
}
