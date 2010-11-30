/**  
* SemanticWebBuilder es una plataforma para el desarrollo de portales y aplicaciones de integración, 
* colaboración y conocimiento, que gracias al uso de tecnología semántica puede generar contextos de 
* información alrededor de algún tema de interés o bien integrar información y aplicaciones de diferentes 
* fuentes, donde a la información se le asigna un significado, de forma que pueda ser interpretada y 
* procesada por personas y/o sistemas, es una creación original del Fondo de Información y Documentación 
* para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite. 
* 
* INFOTEC pone a su disposición la herramienta SemanticWebBuilder a través de su licenciamiento abierto al público (‘open source’), 
* en virtud del cual, usted podrá usarlo en las mismas condiciones con que INFOTEC lo ha diseñado y puesto a su disposición; 
* aprender de él; distribuirlo a terceros; acceder a su código fuente y modificarlo, y combinarlo o enlazarlo con otro software, 
* todo ello de conformidad con los términos y condiciones de la LICENCIA ABIERTA AL PÚBLICO que otorga INFOTEC para la utilización 
* del SemanticWebBuilder 4.0. 
* 
* INFOTEC no otorga garantía sobre SemanticWebBuilder, de ninguna especie y naturaleza, ni implícita ni explícita, 
* siendo usted completamente responsable de la utilización que le dé y asumiendo la totalidad de los riesgos que puedan derivar 
* de la misma. 
* 
* Si usted tiene cualquier duda o comentario sobre SemanticWebBuilder, INFOTEC pone a su disposición la siguiente 
* dirección electrónica: 
*  http://www.webbuilder.org.mx 
**/ 
 
using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows.Forms;

namespace TSWizards.Controls
{
	/// <summary>
	/// Summary description for LineFrame.
	/// </summary>
	public class EtchedLine : System.Windows.Forms.Control
	{
		public EtchedLine()
		{
		}

		protected override void OnPaint(PaintEventArgs pe)
		{
			// TODO: Add custom paint code here
			Graphics g = pe.Graphics;
			using(Pen p = new Pen(Color.FromArgb(128, 128, 128)))
			{
				g.DrawLine(p, new Point(0, 0), new Point(Width, 0));
			}
			g.DrawLine(Pens.White, new Point(0, 1), new Point(Width, 1));

			// Calling the base class OnPaint
			base.OnPaint(pe);
		}

		protected override Size DefaultSize
		{
			get
			{
				return new Size(75, 2);
			}
		}

		[Browsable(true)]
		[Category("Layout")]
		[Description("Gets/Sets the width of the line")]
		public new int Width
		{
			get
			{
				return Size.Width;
			}
			set
			{
				Size = new Size(value, 2);
			}
		}
	}
}
