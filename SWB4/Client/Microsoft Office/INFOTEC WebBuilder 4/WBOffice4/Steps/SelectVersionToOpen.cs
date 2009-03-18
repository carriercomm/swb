﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using WBOffice4.Interfaces;
using WBOffice4.Forms;
namespace WBOffice4.Steps
{
    internal partial class SelectVersionToOpen : TSWizards.BaseInteriorStep
    {
        public static readonly String VERSION = "VERSION";        
        public SelectVersionToOpen()
        {
            InitializeComponent();            
        }

        private void SelectVersionToOpen_ShowStep(object sender, TSWizards.ShowStepEventArgs e)
        {
            string contentID = ((ContentInfo)this.Wizard.Data[Search.CONTENT]).id;
            string repository = this.Wizard.Data[Search.REPOSITORY_ID].ToString();
            this.listView1.Items.Clear();
            foreach (VersionInfo version in OfficeApplication.OfficeDocumentProxy.getVersions(repository, contentID))
            {
                ListViewItem item = new ListViewItem(version.nameOfVersion);
                item.Tag = version;
                String date = String.Format(OfficeApplication.iso8601dateFormat,version.created);
                item.SubItems.Add(date);
                item.SubItems.Add(version.user);
                this.listView1.Items.Add(item);
            }
        }

        private void SelectVersionToOpen_ValidateStep(object sender, CancelEventArgs e)
        {
            if (this.listView1.SelectedIndices.Count == 0)
            {
                MessageBox.Show(this, "¡Debe indicar una versión a abrir", "Selección de Versión", MessageBoxButtons.OK, MessageBoxIcon.Error);
                e.Cancel = true;
            }
            else
            {
                this.Wizard.Data[VERSION] = this.listView1.SelectedItems[0].Tag;
            }
        }

        private void toolStripButtonViewContent_Click(object sender, EventArgs e)
        {
            if (this.listView1.SelectedItems.Count > 0)
            {
                string repository = this.Wizard.Data[Search.REPOSITORY_ID].ToString();
                VersionInfo version = (VersionInfo)this.listView1.SelectedItems[0].Tag;
                String name = null;
                try
                {
                    name = OfficeApplication.OfficeDocumentProxy.createPreview(repository, version.contentId, version.nameOfVersion);
                    String urlproxy = OfficeApplication.OfficeDocumentProxy.WebAddress.ToString();
                    Uri url = new Uri(urlproxy + "?contentId=" + version.contentId + "&versionName=" + version.nameOfVersion + "&repositoryName=" + repository + "&name=" + name);
                    String title = OfficeApplication.OfficeDocumentProxy.getTitle(repository, version.contentId);
                    FormPreview formPreview = new FormPreview(url,false,title);
                    formPreview.ShowDialog(this);                    
                }
                finally
                {
                    if (name != null)
                    {
                        OfficeApplication.OfficeDocumentProxy.deletePreview(name);
                    }
                }

            }

        }
    }
}
