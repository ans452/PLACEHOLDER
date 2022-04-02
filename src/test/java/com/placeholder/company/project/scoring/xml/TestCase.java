package com.placeholder.company.project.scoring.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;

@XmlRootElement( name = "testcase" )
@XmlAccessorType( XmlAccessType.FIELD )
public class TestCase {

	@XmlAttribute( name = "name" )
	private String name;

	@XmlAttribute( name = "classname" )
	private String className;

	@XmlAttribute( name = "time" )
	private Double time;

	@XmlElement( name = "failure" )
	private String failure;

	private TestCase() {

	}

	public static class TestCaseBuilder {

		private final TestCase testCase;

		public TestCaseBuilder() {
			this.testCase = new TestCase();
		}

		public TestCaseBuilder withName( String name ) {
			this.testCase.name = name;
			return this;
		}

		public TestCaseBuilder withClassName( String className ) {
			this.testCase.className = className;
			return this;
		}

		public TestCaseBuilder withTime( Double time ) {
			this.testCase.time = time;
			return this;
		}

		public TestCaseBuilder withFailure( String failure ) {
			this.testCase.failure = failure;
			return this;
		}

		public TestCase build() {
			return testCase;
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "name", name )
				.add( "className", className )
				.add( "time", time )
				.add( "failure", failure )
				.toString();
	}
}