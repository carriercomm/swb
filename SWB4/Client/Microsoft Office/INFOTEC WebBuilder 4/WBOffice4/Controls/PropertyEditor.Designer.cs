﻿using System.ComponentModel;
using System.Windows.Forms;
namespace Editor
{
    partial class PropertyEditor : UserControl
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
            this.paneProperties = new System.Windows.Forms.Panel();
            this.label1 = new System.Windows.Forms.Label();
            this.paneProperties.SuspendLayout();
            this.SuspendLayout();
            // 
            // paneProperties
            // 
            this.paneProperties.AutoScroll = true;
            this.paneProperties.Controls.Add(this.label1);
            this.paneProperties.Dock = System.Windows.Forms.DockStyle.Fill;
            this.paneProperties.Location = new System.Drawing.Point(0, 0);
            this.paneProperties.Name = "paneProperties";
            this.paneProperties.Size = new System.Drawing.Size(356, 262);
            this.paneProperties.TabIndex = 0;
            // 
            // label1
            // 
            this.label1.BackColor = System.Drawing.Color.White;
            this.label1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.label1.Location = new System.Drawing.Point(0, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(356, 262);
            this.label1.TabIndex = 0;
            this.label1.Text = "No hay propiedades que presentar";
            // 
            // PropertyEditor
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.paneProperties);
            this.Name = "PropertyEditor";
            this.Size = new System.Drawing.Size(356, 262);
            this.paneProperties.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ToolTip toolTip1;
        private System.Windows.Forms.Panel paneProperties;
        private Label label1;
    }
}
