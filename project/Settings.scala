import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

/**
 * Application settings. Configure the build for your application here.
 * You normally don't have to touch the actual build definition after this.
 */
object Settings {
  /** The name of your application */
  val name = "scalajs-spa"

  /** The version of your application */
  val version = "1.1.5"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    val scala = "2.12.2"
    val scalaDom = "0.9.3"
    val scalajsReact = "1.1.0"
    val scalaCSS = "0.5.3"
    val log4js = "1.4.13-1"
    val autowire = "0.2.6"
    val booPickle = "1.2.6"
    val diode = "1.1.2"
    val uTest = "0.4.8"

    val react = "15.5.4"
    val jQuery = "3.2.1"
    val bootstrap = "3.3.7-1"
    val chartjs = "2.4.0"

    val scalajsScripts = "1.1.1"
  }

  /**
   * These dependencies are shared between JS and JVM projects
   * the special %%% function selects the correct version for each project
   */
  val sharedDependencies = Def.setting(Seq(
    "com.lihaoyi" %%% "autowire" % versions.autowire,
    "io.suzaku" %%% "boopickle" % versions.booPickle
  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(Seq(
    "com.vmunier" %% "scalajs-scripts" % versions.scalajsScripts,
    "org.webjars" % "font-awesome" % "4.7.0" % Provided,
    "org.webjars" % "bootstrap" % versions.bootstrap % Provided,
    "com.lihaoyi" %% "utest" % versions.uTest % Test
    /* "ws", */
    /* "guice", */
    //"org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
    "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
    "com.github.japgolly.scalacss" %%% "ext-react" % versions.scalaCSS,
    "io.suzaku" %%% "diode" % versions.diode,
    "io.suzaku" %%% "diode-react" % versions.diode,
    "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
    "com.lihaoyi" %%% "utest" % versions.uTest % Test
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
    "org.webjars.bower" % "react" % versions.react / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % versions.react / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
    "org.webjars" % "jquery" % versions.jQuery / "jquery.js" minified "jquery.min.js",
    "org.webjars" % "bootstrap" % versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js",
    "org.webjars" % "chartjs" % versions.chartjs / "Chart.js" minified "Chart.min.js",
    "org.webjars" % "log4javascript" % versions.log4js / "js/log4javascript_uncompressed.js" minified "js/log4javascript.js"
  ))

  val jvmOpts = Map(
    // Turn on HTTPS, turn off HTTP.
    // This should be https://example.com:9443
    "http.port" -> "disabled",
    "https.port" -> "9443",

    // Note that using the HTTPS port by itself doesn't set rh.secure=true.
    // rh.secure will only return true if the "X-Forwarded-Proto" header is set, and
    // if the value in that header is "https", if either the local address is 127.0.0.1, or if
    // trustxforwarded is configured to be true in the application configuration file.

    // Define the SSLEngineProvider in our own class.
    "play.http.sslengineprovider" -> "https.CustomSSLEngineProvider",

    // Enable this if you want to turn on client authentication
    //"play.ssl.needClientAuth" -> "true",

    // Enable the handshake parameter to be extended for better protection.
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html//customizing_dh_keys
    // Only relevant for "DHE_RSA", "DHE_DSS", "DH_ANON" algorithms, in ServerHandshaker.java.
    "jdk.tls.ephemeralDHKeySize" -> "2048",

    // Don't allow client to dictate terms - this can also be used for DoS attacks.
    // Undocumented, defined in sun.security.ssl.Handshaker.java:205
    "jdk.tls.rejectClientInitiatedRenegotiation" -> "true",

    // Add more details to the disabled algorithms list
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html//DisabledAlgorithms
    // and http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7133344
    "java.security.properties" -> "disabledAlgorithms.properties",


    // Fix a version number problem in SSLv3 and TLS version 1.0.
    // http://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html
    "com.sun.net.ssl.rsaPreMasterSecretFix" -> "true",

    // Tighten the TLS negotiation issue.
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html//descPhase2
    // Defined in JDK 1.8 sun.security.ssl.Handshaker.java:194
    "sun.security.ssl.allowUnsafeRenegotiation" -> "false",
    "sun.security.ssl.allowLegacyHelloMessages" -> "false"

    // Enable this if you need to use OCSP or CRL
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html//AppC
    //"com.sun.security.enableCRLDP" -> "true",
    //"com.sun.net.ssl.checkRevocation" -> "true"

    // Enable this if you need TLS debugging
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html//Debug
    //"javax.net.debug" -> "ssl:handshake"

    // Change this if you need X.509 certificate debugging
    // http://docs.oracle.com/javase/8/docs/technotes/guides/security/troubleshooting-security.html
    //"java.security.debug" -> "certpath:x509:ocsp"

    // Uncomment if you want to run "./play client" explicitly without SNI.
    //"jsse.enableSNIExtension" -> "false"
  )
}
