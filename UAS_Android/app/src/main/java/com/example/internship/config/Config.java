package com.example.internship.config;

public interface Config {
    public static final String IP = "192.168.0.109";

    public static final String getDataAdm = "http://" + IP +"/intern/tampil_proj.php";
    public static final String getDataNonAdm = "http://" + IP +"/intern/tampil_projnonadm.php";
    public static final String getDataDetail = "http://" + IP +"/intern/detail_proj.php";
    public static final String getDataIntern = "http://" + IP +"/intern/tampil_intern.php";
    public static final String getDataInternNonAdm = "http://" + IP +"/intern/tampil_internnonadm.php";
    public static final String getDataDetailIntern = "http://" + IP +"/intern/detail_intern.php";
    public static final String getDataDetailInternNonAdm = "http://" + IP +"/intern/detail_internnonadm.php";
    public static final String loginPhp = "http://" + IP +"/intern/login.php";
    public static final String registerPhp = "http://" + IP +"/intern/register.php";
    public static final String addProject = "http://" + IP +"/intern/addProject.php";
    public static final String addDetailProject = "http://" + IP +"/intern/addDetailProject.php";
    public static final String spinnerName = "http://" + IP +"/intern/spinnerName.php";
    public static final String deleteProject = "http://" + IP +"/intern/deleteProject.php";
    public static final String editProject = "http://" + IP +"/intern/editProject.php";
    public static final String deleteIntern = "http://" + IP +"/intern/deleteIntern.php";
    public static final String editIntern = "http://" + IP +"/intern/editIntern.php";
}
