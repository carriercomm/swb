﻿namespace WBOffice4.Forms
{
    partial class FormAddLink
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

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FormAddLink));
            this.SuspendLayout();
            // 
            // panelStep
            // 
            this.panelStep.BackColor = System.Drawing.SystemColors.Control;
            this.panelStep.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelStep.Location = new System.Drawing.Point(0, 66);
            this.panelStep.Size = new System.Drawing.Size(488, 249);
            // 
            // FormAddLink
            // 
            this.ClientSize = new System.Drawing.Size(488, 355);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "FormAddLink";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Asistente para insertar una liga";
            this.VisibleLogo = true;
            this.LoadSteps += new System.EventHandler(this.FormAddLink_LoadSteps);
            this.ResumeLayout(false);

        }

        #endregion
    }
}
