package TACSCMApp;


/**
* TACSCMApp/SCMPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from TACSCM.idl
* 28. august 2014 kl 12.03 CEST
*/

public abstract class SCMPOA extends org.omg.PortableServer.Servant
 implements TACSCMApp.SCMOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("send", new java.lang.Integer (0));
    _methods.put ("status", new java.lang.Integer (1));
    _methods.put ("getTime", new java.lang.Integer (2));
    _methods.put ("getInterestRate", new java.lang.Integer (3));
    _methods.put ("getLoanInterestRate", new java.lang.Integer (4));
    _methods.put ("getStorageCost", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // TACSCMApp/SCM/send
       {
         String str = in.read_string ();
         String $result = null;
         $result = this.send (str);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // TACSCMApp/SCM/status
       {
         boolean $result = false;
         $result = this.status ();
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // TACSCMApp/SCM/getTime
       {
         short $result = (short)0;
         $result = this.getTime ();
         out = $rh.createReply();
         out.write_short ($result);
         break;
       }

       case 3:  // TACSCMApp/SCM/getInterestRate
       {
         double $result = (double)0;
         $result = this.getInterestRate ();
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 4:  // TACSCMApp/SCM/getLoanInterestRate
       {
         double $result = (double)0;
         $result = this.getLoanInterestRate ();
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 5:  // TACSCMApp/SCM/getStorageCost
       {
         double $result = (double)0;
         $result = this.getStorageCost ();
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:TACSCMApp/SCM:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public SCM _this() 
  {
    return SCMHelper.narrow(
    super._this_object());
  }

  public SCM _this(org.omg.CORBA.ORB orb) 
  {
    return SCMHelper.narrow(
    super._this_object(orb));
  }


} // class SCMPOA
