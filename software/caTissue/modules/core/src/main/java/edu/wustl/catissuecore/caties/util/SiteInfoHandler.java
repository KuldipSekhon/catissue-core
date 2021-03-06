/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.caties.util;

/**
 * <p>Title:XMLPropertyHandler Class>
 * <p>Description:This class parses from caTissue_Properties.xml
 * (includes properties name & value pairs) file using DOM parser.</p>.
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Tapan Sahoo
 * @version 1.00
 * Created on May 15, 2006
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wustl.catissuecore.domain.Site;


/**
 * This class gives the properties value by giving properties name.
 * @author tapan_sahoo
 */
public class SiteInfoHandler
{

	/**
	 * logger.
	 */

	/**
	 * document.
	 */
	private static Document document = null;
	public static Map<String, List<String>> siteAbrMap = new HashMap<String, List<String>>();

	/**
	 * Initialization method.
	 * @param path path of file name
	 * @throws Exception generic exception
	 */
	public static void init(String path) throws Exception
	{

		final DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (final Exception exp)
		{

			throw exp;
		}
	}

	public static void validateAndCreateMapOfSites() throws Exception
	{
		// it gives the rootNode of the xml file
		final Element sitesRoot = document.getDocumentElement();
		final NodeList siteNode = sitesRoot.getElementsByTagName("site");
		int totalSiteNode = siteNode.getLength();

		for (int s = 0; s < totalSiteNode; s++)
		{
			List<String> siteNames = new LinkedList<String>();
			Node firstSiteNode = siteNode.item(s);
			Element firstSiteElement = (Element) firstSiteNode;

			NodeList abrList = firstSiteElement.getElementsByTagName("SITE_ABBRIVIATION");
			Element abrElement = (Element) abrList.item(0);

			NodeList textLNList = abrElement.getChildNodes();
			String abr = ((Node) textLNList.item(0)).getNodeValue().trim();


			NodeList siteList = firstSiteElement.getElementsByTagName("SITE_NAME");

			for (int i = 0; i < siteList.getLength(); i++)
			{
				Node site = siteList.item(i);
				Element siteElement = (Element) site;

				NodeList textSiteNameList = siteElement.getChildNodes();
				String siteName = ((Node) textSiteNameList.item(0)).getNodeValue().trim();

				if (siteName != null)
				{

					final String siteHql = "select count(*)"
							+ " from edu.wustl.catissuecore.domain.Site as site "
							+ " where site.name='" + siteName + "' ";

					final List<Integer> sitResultList = CaCoreAPIService.executeQuery(siteHql,
							Integer.class);


					if (sitResultList != null && sitResultList.size() != 0
							&& Integer.parseInt(sitResultList.get(0).toString()) <= 0)
					{
						//logger.info("got site");

						throw new Exception("SITE_NAME  " + siteName
								+ " given in xml not valid for the SITE_ABBRIVIATION  " + abr);
					}
					siteNames.add(siteName);

				}
			} // for site ends

			siteAbrMap.put(abr, siteNames);
		} // for ends
	}
}