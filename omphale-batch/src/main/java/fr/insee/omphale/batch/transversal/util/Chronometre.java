package fr.insee.omphale.batch.transversal.util;

 import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

 /**
  * Chronomètre utilisé pour calculer la durée de la projection.
  *
  */
 public class Chronometre {
 private Calendar m_start = new GregorianCalendar();
 private Calendar m_stop = new GregorianCalendar();

 public Chronometre() {
 }

 //Lance le chronomètre
 public void start() {
 m_start.setTime(new Date());
 }

 //Arrète le chronomètre
 public void stop() {
 m_stop.setTime(new Date());
 }

 //Retourne le nombre de millisecondes séparant l'appel des méthode start() et stop()
 public long getMilliSec() {
  return (m_stop.getTimeInMillis() - m_start.getTimeInMillis());
 }
 
 //Retourne le nombre de minutes séparant l'appel des méthode start() et stop()
 public double getMinutes() {
     double db=getMilliSec();
     double cimil=60000;
     return round((db/cimil), 2);
 }

 //méthode d'arrondi
 private double round(double what, int howmuch) {
     return (double)( (int)(what * Math.pow(10,howmuch) + .5) ) / Math.pow(10,howmuch);
 }

 }